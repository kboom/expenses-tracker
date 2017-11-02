package com.ggurgul.playground.extracker.auth.security

import com.ggurgul.playground.extracker.auth.models.IdentityType
import com.ggurgul.playground.extracker.auth.services.ExternalIdentityExtractor
import com.ggurgul.playground.extracker.auth.services.LocalUserDetailsService
import com.ggurgul.playground.extracker.auth.services.UserPrincipal
import com.ggurgul.playground.extracker.auth.services.UserPrincipalModel
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
import org.springframework.security.config.http.SessionCreationPolicy
import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.security.core.userdetails.User
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.security.oauth2.client.OAuth2ClientContext
import org.springframework.security.oauth2.client.OAuth2RestTemplate
import org.springframework.security.oauth2.client.filter.OAuth2ClientAuthenticationProcessingFilter
import org.springframework.security.oauth2.client.filter.OAuth2ClientContextFilter
import org.springframework.security.oauth2.client.token.grant.code.AuthorizationCodeResourceDetails
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableOAuth2Client
import org.springframework.security.web.authentication.LoginUrlAuthenticationEntryPoint
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter
import org.springframework.web.bind.annotation.CrossOrigin
import org.springframework.web.filter.CompositeFilter
import java.util.*
import javax.servlet.Filter

@Configuration
@EnableOAuth2Client
@Order(SecurityProperties.ACCESS_OVERRIDE_ORDER)
class WebSecurityConfig : WebSecurityConfigurerAdapter() {

    @Autowired
    private lateinit var oauth2ClientContext: OAuth2ClientContext

    @Autowired
    private lateinit var userDetailsService: LocalUserDetailsService

    @Throws(Exception::class)
    override fun configure(auth: AuthenticationManagerBuilder) {
        auth.userDetailsService(userDetailsService).passwordEncoder(passwordEncoder())
    }

    // new SavedRequestAwareAuthenticationSuccessHandler()
    @Throws(Exception::class)
    override fun configure(http: HttpSecurity) {
        // .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
        http.antMatcher("/**")
                .formLogin()
//                .httpBasic()
                .and()
                .authorizeRequests()
                .antMatchers("/", "/login**", "/webjars/**").permitAll()
                .antMatchers("/registration/**").permitAll()
                .antMatchers(HttpMethod.POST, "/auth/account/password/reset*/**").permitAll()
                .anyRequest()
                .authenticated()
                .and()
                .exceptionHandling()
                .authenticationEntryPoint(LoginUrlAuthenticationEntryPoint("/"))
                .and().logout().logoutSuccessUrl("/").permitAll().and()
                // .and().csrf().csrfTokenRepository(CookieCsrfTokenRepository.withHttpOnlyFalse())
                .csrf().disable() // todo consider how to enable this only for parts of the service which is exposed to the web browser
                .addFilterBefore(createClientFilter(), BasicAuthenticationFilter::class.java)
    }

    @Bean
    fun passwordEncoder(): PasswordEncoder = BCryptPasswordEncoder()

    @Bean
    fun predefinedUsers(): Map<String, UserPrincipal> = listOf(adminUser()).map {
        UserPrincipalModel(User(
                it.username,
                passwordEncoder().encode(it.password),
                mutableListOf(SimpleGrantedAuthority("ADMIN"))
        ))
    }.associateBy { it.username }

    @Bean
    @ConfigurationProperties("admin")
    fun adminUser() = PredefinedUserResource()

    // https://github.com/spring-guides/tut-spring-security-and-angular-js/blob/master/oauth2/authserver/src/main/java/demo/AuthserverApplication.java

    @Bean
    @ConfigurationProperties("github")
    fun github() = ClientResource(ExternalIdentityExtractor(
            idKey = "sub",
            identityType = IdentityType.GITHUB,
            mailKey = "email",
            usernameKey = "username",
            userDetailsService = userDetailsService
    ))

    @Bean
    @ConfigurationProperties("facebook")
    fun facebook() = ClientResource(ExternalIdentityExtractor(
            idKey = "sub",
            identityType = IdentityType.FACEBOOK,
            mailKey = "email",
            usernameKey = "username",
            userDetailsService = userDetailsService
    ))

    @Bean
    @ConfigurationProperties("google")
    fun google() = ClientResource(ExternalIdentityExtractor(
            idKey = "sub",
            identityType = IdentityType.GOOGLE,
            mailKey = "email",
            usernameKey = "username",
            userDetailsService = userDetailsService
    ))

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
        tokenServices.setPrincipalExtractor(client.identityExtractor)
        tokenServices.setAuthoritiesExtractor(client.identityExtractor)
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

            val identityExtractor: ExternalIdentityExtractor,

            @NestedConfigurationProperty
            val client: AuthorizationCodeResourceDetails = AuthorizationCodeResourceDetails(),

            @NestedConfigurationProperty
            val resource: ResourceServerProperties = ResourceServerProperties()

    )

    /**
     * This class has to have public getters for property injection to work.
     */
    class PredefinedUserResource(

            var username: String = "",

            var password: String = ""

    )

}
