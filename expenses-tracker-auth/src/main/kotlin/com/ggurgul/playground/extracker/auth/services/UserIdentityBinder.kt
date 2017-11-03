package com.ggurgul.playground.extracker.auth.services

import com.ggurgul.playground.extracker.auth.models.*
import com.ggurgul.playground.extracker.auth.repositories.UserRepository
import org.springframework.context.ApplicationListener
import org.springframework.security.authentication.event.AuthenticationSuccessEvent
import org.springframework.security.oauth2.provider.OAuth2Authentication
import org.springframework.stereotype.Component
import java.util.*

@Component
class UserIdentityBinder(
        val userDetailsService: LocalUserDetailsService,
        val userRepository: UserRepository
) : ApplicationListener<AuthenticationSuccessEvent> {

    override fun onApplicationEvent(evt: AuthenticationSuccessEvent) {
        when (evt.authentication.principal) {
            is ExternalPrincipal -> createOrBindUser(evt.authentication as OAuth2Authentication)
        }
    }

    private fun createOrBindUser(auth: OAuth2Authentication) {
        val principal = auth.principal as ExternalPrincipal
        val boundUser = userDetailsService.findByIdentity(principal.identity, principal.identityType)

        if (!boundUser.isPresent) {
            val unboundUser = userDetailsService.findByEmail(principal.email)
            if (unboundUser.isPresent) {
                bindNewIdentity(unboundUser, principal)
            } else {
                createNewUser(auth)
            }
        }

        // todo probably authentication should be swapper to a normal one at this point (the local one as we have the user now)
    }

    private fun createNewUser(auth: OAuth2Authentication) {
        val principal = auth.principal as ExternalPrincipal
        userRepository.save(
                User(
                        email = principal.email,
                        firstName = principal.firstName,
                        lastName = principal.lastName,
                        enabled = true,
                        authorities = auth.authorities.map { Authority(AuthorityName.valueOf(it.authority!!)) }
                                .toMutableList()
                ).apply {
                    boundIdentities = mutableListOf(createIdentity(principal.identity, principal.identityType))
                }
        )
    }

    private fun bindNewIdentity(unboundUser: Optional<User>, principal: ExternalPrincipal) {
        userRepository.save(
                unboundUser.get().apply {
                    boundIdentities.add(createIdentity(principal.identity, principal.identityType))
                }
        )
    }

    private fun User.createIdentity(identity: String, identityType: IdentityType): BoundIdentity {
        return BoundIdentity(
                user = this,
                internalId = identity,
                identityType = identityType
        )
    }


}