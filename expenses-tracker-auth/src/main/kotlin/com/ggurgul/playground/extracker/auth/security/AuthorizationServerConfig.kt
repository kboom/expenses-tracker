package com.ggurgul.playground.extracker.auth.security

import com.ggurgul.playground.extracker.auth.services.LocalUserDetailsService
import com.ggurgul.playground.extracker.auth.services.UserPrincipal
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.Primary
import org.springframework.core.io.ClassPathResource
import org.springframework.http.MediaType
import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.oauth2.common.DefaultOAuth2AccessToken
import org.springframework.security.oauth2.common.OAuth2AccessToken
import org.springframework.security.oauth2.config.annotation.configurers.ClientDetailsServiceConfigurer
import org.springframework.security.oauth2.config.annotation.web.configuration.AuthorizationServerConfigurerAdapter
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableAuthorizationServer
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerEndpointsConfigurer
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerSecurityConfigurer
import org.springframework.security.oauth2.provider.OAuth2Authentication
import org.springframework.security.oauth2.provider.token.*
import org.springframework.security.oauth2.provider.token.store.JwtAccessTokenConverter
import org.springframework.security.oauth2.provider.token.store.JwtTokenStore
import org.springframework.security.oauth2.provider.token.store.KeyStoreKeyFactory
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import java.security.Principal
import java.util.*


@Configuration
@EnableAuthorizationServer
@RestController
class AuthorizationServerConfig : AuthorizationServerConfigurerAdapter() {

    @Autowired
    private lateinit var authenticationManager: AuthenticationManager

    @Autowired
    private lateinit var userDetailsService: LocalUserDetailsService

    @Value("\${keystore.password}")
    private val pwd: String? = null

    /**
     * This endpoint gets called whenever the user becomes authenticated via oauth.
     */
    @RequestMapping(path = arrayOf("/user"), produces = arrayOf(MediaType.ALL_VALUE))
    fun user(principal: Principal?): Principal? {
//        return (principal as OAuth2Authentication).userAuthentication.principal as UserPrincipal
return principal
        // should create user if not already present, gets in here only when social login
    }

    // should have login form

    @Throws(Exception::class)
    override fun configure(oauthServer: AuthorizationServerSecurityConfigurer?) {
        oauthServer!!.tokenKeyAccess("permitAll()").checkTokenAccess("isAuthenticated()")
    }

    @Throws(Exception::class)
    override fun configure(clients: ClientDetailsServiceConfigurer?) {
        clients!!.inMemory()
                .withClient("expenses-tracker-service")
                .secret("expenses-tracker-service-secret")
                .authorizedGrantTypes("client_credentials", "password", "authorization_code", "refresh_token")
                .accessTokenValiditySeconds(3600)
                .refreshTokenValiditySeconds(2592000)
                .redirectUris("http://localhost:9999/api/login")
                .scopes("read", "write")
                .autoApprove(".*")
    }

    @Throws(Exception::class)
    override fun configure(endpoints: AuthorizationServerEndpointsConfigurer?) {
        endpoints!!
                .tokenStore(tokenStore())
                .tokenEnhancer(TokenEnhancerChain().apply {
                    setTokenEnhancers(Arrays.asList(CustomTokenEnhancer(), jwtTokenConverter()))
                })
                .authenticationManager(authenticationManager)
    }

    @Bean
    @Primary
    fun tokenServices(): DefaultTokenServices {
        val defaultTokenServices = DefaultTokenServices()
        defaultTokenServices.setTokenStore(tokenStore())
        defaultTokenServices.setSupportRefreshToken(true)
        return defaultTokenServices
    }

    @Bean
    @Primary
    fun tokenStore(): JwtTokenStore {
        return JwtTokenStore(jwtTokenConverter())
    }

    @Bean
    protected fun jwtTokenConverter(): JwtAccessTokenConverter {
        val keyStoreKeyFactory = KeyStoreKeyFactory(
                ClassPathResource("jwt.jks"),
                pwd!!.toCharArray()
        )
        val converter = JwtAccessTokenConverter()
        converter.setKeyPair(keyStoreKeyFactory.getKeyPair("jwt"))
        converter.accessTokenConverter = DefaultAccessTokenConverter().apply {
            setUserTokenConverter(
                    // todo use extended DefaultUserAuthenticationConverter (use super + extract user details from the provided JWT map
                    // todo no need to contact database, all details already there (especially important if it was being done in remote application)
                    DefaultUserAuthenticationConverter().apply {
                        setUserDetailsService(userDetailsService)
                        // todo this will make principal of SecurityContext UserPrincipal
                    }
            )
        }
        return converter;
    }

    class CustomTokenEnhancer : TokenEnhancer {

        override fun enhance(accessToken: OAuth2AccessToken, authentication: OAuth2Authentication): OAuth2AccessToken {
            val additionalInfo = HashMap<String, Any>()
            val authenticatedUser = (authentication.principal as UserPrincipal).user
            additionalInfo.put("email", authenticatedUser.email)
            (accessToken as DefaultOAuth2AccessToken).additionalInformation = additionalInfo
            return accessToken
        }

    }

}
