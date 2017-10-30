package com.ggurgul.playground.extracker.auth.security

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.autoconfigure.security.SecurityProperties
import org.springframework.boot.autoconfigure.security.oauth2.resource.ResourceServerProperties
import org.springframework.boot.autoconfigure.security.oauth2.resource.UserInfoTokenServices
import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.boot.context.properties.NestedConfigurationProperty
import org.springframework.boot.web.servlet.FilterRegistrationBean
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.core.annotation.Order
import org.springframework.http.HttpMethod
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter
import org.springframework.security.oauth2.client.OAuth2ClientContext
import org.springframework.security.oauth2.client.OAuth2RestTemplate
import org.springframework.security.oauth2.client.filter.OAuth2ClientAuthenticationProcessingFilter
import org.springframework.security.oauth2.client.filter.OAuth2ClientContextFilter
import org.springframework.security.oauth2.client.token.grant.code.AuthorizationCodeResourceDetails
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableOAuth2Client
import org.springframework.security.web.authentication.LoginUrlAuthenticationEntryPoint
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter
import org.springframework.security.web.csrf.CookieCsrfTokenRepository
import org.springframework.web.filter.CompositeFilter
import java.util.*
import javax.servlet.Filter

@Configuration
@EnableOAuth2Client
@Order(SecurityProperties.ACCESS_OVERRIDE_ORDER)
class WebSecurityConfig : WebSecurityConfigurerAdapter() {

    @Autowired
    private lateinit var oauth2ClientContext: OAuth2ClientContext

    @Throws(Exception::class)
    override fun configure(auth: AuthenticationManagerBuilder?) {
        auth!!.inMemoryAuthentication()
                .withUser("kboom").password("secret").roles("ADMIN")
    }

    @Throws(Exception::class)
    override fun configure(http: HttpSecurity) {
        http.antMatcher("/**")
                .authorizeRequests()
                .antMatchers(HttpMethod.POST, "/account/password/reset*/**").permitAll()
                .antMatchers("/auth/**", "/registration/**").permitAll()
                .antMatchers("/", "/login**", "/webjars/**").permitAll()
                .anyRequest()
                .authenticated()
                .and()
                .exceptionHandling()
                .authenticationEntryPoint(LoginUrlAuthenticationEntryPoint("/")).and().logout()
                .logoutSuccessUrl("/").permitAll()
                // .and().csrf().csrfTokenRepository(CookieCsrfTokenRepository.withHttpOnlyFalse())
                .and().csrf().disable() // todo consider how to enable this only for parts of the service which is exposed to the web browser
                .addFilterBefore(createClientFilter(), BasicAuthenticationFilter::class.java)
    }

    @Bean
    @ConfigurationProperties("github")
    fun github(): ClientResource {
        return ClientResource()
    }

    @Bean
    @ConfigurationProperties("facebook")
    fun facebook(): ClientResource {
        return ClientResource()
    }

    @Bean
    @ConfigurationProperties("google")
    fun google(): ClientResource {
        return ClientResource()
    }

    private fun createClientFilter(): Filter {
        val filter = CompositeFilter()
        val filters = ArrayList<Filter>()
        filters.add(createClientFilter(facebook(), "/login/facebook"))
        filters.add(createClientFilter(google(), "/login/google"))
        filters.add(createClientFilter(github(), "/login/github"))
        filter.setFilters(filters)
        return filter
    }

    private fun createClientFilter(client: ClientResource, path: String): Filter {
        val filter = OAuth2ClientAuthenticationProcessingFilter(
                path)
        val template = OAuth2RestTemplate(client.client, oauth2ClientContext)
        filter.setRestTemplate(template)
        val tokenServices = UserInfoTokenServices(
                client.resource.userInfoUri, client.client.clientId)
        tokenServices.setRestTemplate(template)
        filter.setTokenServices(tokenServices)
        return filter
    }


    @Bean
    fun oauth2ClientFilterRegistration(filter: OAuth2ClientContextFilter): FilterRegistrationBean {
        val registration = FilterRegistrationBean()
        registration.filter = filter
        registration.order = -100
        return registration
    }

    /**
     * This class has to have public getters for property injection to work.
     */
    class ClientResource(

            @NestedConfigurationProperty
            val client: AuthorizationCodeResourceDetails = AuthorizationCodeResourceDetails(),

            @NestedConfigurationProperty
            val resource: ResourceServerProperties = ResourceServerProperties()

    )

}
