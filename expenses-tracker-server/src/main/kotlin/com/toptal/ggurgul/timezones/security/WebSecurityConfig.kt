package com.toptal.ggurgul.timezones.security

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.http.HttpMethod
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter
import org.springframework.security.config.http.SessionCreationPolicy
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.security.web.authentication.AnonymousAuthenticationFilter
import org.springframework.web.cors.UrlBasedCorsConfigurationSource
import com.google.common.collect.ImmutableList
import org.springframework.web.cors.CorsConfiguration
import org.springframework.web.cors.CorsConfigurationSource




@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
open class WebSecurityConfig : WebSecurityConfigurerAdapter() {

    @Autowired
    private val unauthorizedHandler: JwtAuthenticationEntryPoint? = null

    @Autowired
    private val userDetailsService: UserDetailsService? = null

    @Autowired
    @Throws(Exception::class)
    fun configureGlobal(authenticationManagerBuilder: AuthenticationManagerBuilder) {
//        authenticationManagerBuilder
//                .inMemoryAuthentication()
//                .withUser("admin").password("admin")
//                .roles(
//                        AuthorityName.USER.toString(),
//                        AuthorityName.MANAGER.toString(),
//                        AuthorityName.ADMIN.toString()
//                )
        authenticationManagerBuilder
                .userDetailsService<UserDetailsService>(userDetailsService)
                .passwordEncoder(passwordEncoder())
    }

    @Bean
    open fun passwordEncoder(): PasswordEncoder {
        return BCryptPasswordEncoder()
    }

    @Bean
    @Throws(Exception::class)
    open fun authenticationTokenFilterBean() = JwtAuthenticationTokenFilter()

    @Bean
    fun corsConfigurationSource(): CorsConfigurationSource {
        val configuration = CorsConfiguration()
        configuration.allowedOrigins = ImmutableList.of("*")
        configuration.allowedMethods = ImmutableList.of("HEAD",
                "GET", "POST", "PUT", "DELETE", "PATCH")
        configuration.allowCredentials = true
        configuration.allowedHeaders = ImmutableList.of("Authorization", "Cache-Control", "Content-Type")
        val source = UrlBasedCorsConfigurationSource()
        source.registerCorsConfiguration("/**", configuration)
        return source
    }

    @Throws(Exception::class)
    override fun configure(httpSecurity: HttpSecurity) {
        httpSecurity
                .cors().and()
                .csrf().disable()
                .exceptionHandling().authenticationEntryPoint(unauthorizedHandler).and()
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and()
                .authorizeRequests()
                .antMatchers(HttpMethod.OPTIONS, "/**").permitAll()
                .antMatchers("/h2-console/**").permitAll()
                .antMatchers(HttpMethod.POST, "/account/password/reset*/**").permitAll()
                .antMatchers(
                        HttpMethod.GET,
                        "/",
                        "/health/**",
                        "/*.html",
                        "/favicon.ico",
                        "/**/*.html",
                        "/**/*.css",
                        "/**/*.js",
                        "/swagger*/**",
                        "/webjars/**",
                        "/v2/api-docs/**"
                ).permitAll()
                .antMatchers("/auth/**", "/registration/**")
                .permitAll()
                .anyRequest()
                .authenticated()

        httpSecurity.addFilterBefore(authenticationTokenFilterBean(), AnonymousAuthenticationFilter::class.java)
        httpSecurity.headers().cacheControl()
    }

}