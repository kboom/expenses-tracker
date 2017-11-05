package com.ggurgul.playground.extracker.auth.services

import com.ggurgul.playground.extracker.auth.models.*
import com.ggurgul.playground.extracker.auth.repositories.UserRepository
import org.springframework.security.core.GrantedAuthority
import org.springframework.stereotype.Component
import java.security.Principal
import java.util.*

interface IdentityBinder {

    fun convertPrincipal(principal: ExternalPrincipal): Principal
    fun getAuthoritiesFrom(principal: ExternalPrincipal): List<GrantedAuthority>

}

@Component
class UserIdentityBinder(
        val userDetailsService: LocalUserDetailsService,
        val userRepository: UserRepository
) : IdentityBinder {

    override fun convertPrincipal(principal: ExternalPrincipal): UserPrincipal {
        val boundUser = userDetailsService.findByIdentity(principal.identity, principal.identityType)
                .orElseGet { bindOrCreateUsingEmail(principal) }

        return UserPrincipalEntity(boundUser)
    }

    /**
     * Although this might look like doing the same thing the second time, it is going to be super fast.
     * The user is already there - cached - so it is just a matter of retrieving him.
     */
    override fun getAuthoritiesFrom(principal: ExternalPrincipal): List<GrantedAuthority> {
        return convertPrincipal(principal).authorities.toList()
    }

    private fun bindOrCreateUsingEmail(principal: ExternalPrincipal): User {
        val unboundUser = userDetailsService.findByEmail(principal.email)
        if (unboundUser.isPresent) {
            return bindNewIdentity(unboundUser, principal)
        } else {
            return createNewUserFrom(principal)
        }
    }

    private fun createNewUserFrom(principal: ExternalPrincipal): User {
        return userRepository.save(
                User(
                        email = principal.email,
                        firstName = principal.firstName,
                        lastName = principal.lastName,
                        enabled = true,
                        authorities = listOf(AuthorityName.ROLE_USER).map { Authority(it) }.toMutableList()
                ).apply {
                    boundIdentities = mutableListOf(createIdentity(principal.identity, principal.identityType))
                }
        )
    }

    private fun bindNewIdentity(unboundUser: Optional<User>, principal: ExternalPrincipal) = userRepository.save(
            unboundUser.get().apply {
                boundIdentities.add(createIdentity(principal.identity, principal.identityType))
            }
    )


    private fun User.createIdentity(identity: String, identityType: IdentityType): BoundIdentity {
        return BoundIdentity(
                user = this,
                internalId = identity,
                identityType = identityType
        )
    }


}