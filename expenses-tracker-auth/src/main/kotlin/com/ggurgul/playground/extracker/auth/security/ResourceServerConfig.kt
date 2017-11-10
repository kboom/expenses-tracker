package com.ggurgul.playground.extracker.auth.security

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.annotation.Configuration
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableResourceServer
import org.springframework.security.oauth2.config.annotation.web.configuration.ResourceServerConfigurerAdapter
import org.springframework.security.oauth2.config.annotation.web.configurers.ResourceServerSecurityConfigurer
import org.springframework.security.oauth2.provider.endpoint.FrameworkEndpointHandlerMapping
import org.springframework.security.web.util.matcher.RequestMatcher
import javax.servlet.http.HttpServletRequest


@Configuration
@EnableResourceServer
class ResourceServerConfig : ResourceServerConfigurerAdapter() {

    @Autowired
    lateinit var oauth2EndpointHandlerMapping: FrameworkEndpointHandlerMapping


    @Throws(Exception::class)
    override fun configure(resources: ResourceServerSecurityConfigurer) {
        resources.resourceId("aaa")
    }

    /**
     * Resources exposed via oauth. As we are providing also local user interface they are also accessible from within.
     * Note that this security filter chain has to be bypassed in case the request is not intended to be OAuth related.
     * This is done by BearerAuthorizationHeaderMatcher which matches only on Authorization Bearer tokens.
     * This will enable the client to access same paths by local login mechanisms (session-based) and oauth simultaneously.
     */
    @Throws(Exception::class)
    override fun configure(http: HttpSecurity) {
        http.requestMatcher(BearerAuthorizationHeaderMatcher())
                .authorizeRequests()
                .anyRequest()
                .authenticated()
    }

    private class BearerAuthorizationHeaderMatcher : RequestMatcher {
        override fun matches(request: HttpServletRequest): Boolean {
            val auth = request.getHeader("Authorization")
            return auth != null && auth.startsWith("Bearer")
        }
    }

}