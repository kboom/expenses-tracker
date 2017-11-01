package com.ggurgul.playground.extracker.auth.services

import com.ggurgul.playground.extracker.auth.models.*
import org.springframework.boot.autoconfigure.security.oauth2.resource.AuthoritiesExtractor
import org.springframework.boot.autoconfigure.security.oauth2.resource.PrincipalExtractor
import org.springframework.security.core.GrantedAuthority

class ExternalIdentityExtractor(
        private val identityType: IdentityType,
        private val idKey: String,
        private val mailKey: String,
        private val usernameKey: String,
        private val userDetailsService: LocalUserDetailsService
) : PrincipalExtractor, AuthoritiesExtractor {

    override fun extractPrincipal(props: MutableMap<String, Any>): Any {
        val identity = props.getOrElse(idKey) { throw IllegalStateException("Could not learn the identity of the user") } as String
        val boundUser = userDetailsService.findByIdentity(identity, identityType)

        if(boundUser.isPresent) {
            return boundUser.get()
        } else {
            if (props.containsKey(mailKey)) {
                val unboundUser = userDetailsService.findByEmail(props[mailKey] as String)
                if(unboundUser.isPresent) {
                    return unboundUser.get().apply {
                        user.boundIdentities.add(createIdentity(identity))
                    }
                }
            }
        }

        return UserPrincipalEntity(User(
                username = "abcdef", //props.getOrDefault(usernameKey, identity) as String,
                email = props.getOrElse(mailKey) { throw IllegalStateException("Bound account does not have e-mail address") } as String,
                authorities = mutableListOf(Authority(AuthorityName.ROLE_USER))
        )).apply {
            user.boundIdentities.add(createIdentity(identity))
        }
    }

    private fun UserPrincipalEntity.createIdentity(identity: String): BoundIdentity {
        return BoundIdentity(
                user = user,
                internalId = identity,
                identityType = identityType
        )
    }

    // there are no authorities from external source allowed
    override fun extractAuthorities(props: MutableMap<String, Any>?): MutableList<GrantedAuthority> {
        return mutableListOf()
    }

}