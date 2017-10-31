package com.ggurgul.playground.extracker.auth.security

import org.springframework.context.annotation.Configuration
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableResourceServer
import org.springframework.security.oauth2.config.annotation.web.configuration.ResourceServerConfigurerAdapter
import org.springframework.security.oauth2.config.annotation.web.configurers.ResourceServerSecurityConfigurer

@Configuration
@EnableResourceServer
@EnableGlobalMethodSecurity(prePostEnabled = true)
class ResourceServerConfig : ResourceServerConfigurerAdapter() {

    @Throws(Exception::class)
    override fun configure(resources: ResourceServerSecurityConfigurer?) {
        resources!!.resourceId("EXTRACKER_AUTH")
    }

    /**
     * Those are resources served by the server. All have to be under /auth path.
     */
    @Throws(Exception::class)
    override fun configure(http: HttpSecurity) {
        http.antMatcher("/auth/**")
                .authorizeRequests()
                .antMatchers("/account").authenticated()
    }

}