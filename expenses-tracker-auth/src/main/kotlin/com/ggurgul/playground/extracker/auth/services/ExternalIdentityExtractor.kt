package com.ggurgul.playground.extracker.auth.services

import com.ggurgul.playground.extracker.auth.models.*
import org.springframework.boot.autoconfigure.security.oauth2.resource.AuthoritiesExtractor
import org.springframework.boot.autoconfigure.security.oauth2.resource.PrincipalExtractor
import org.springframework.security.core.GrantedAuthority
import org.springframework.security.core.authority.SimpleGrantedAuthority

class ExternalIdentityExtractor(
        private val identityType: IdentityType,
        private val idKey: String,
        private val mailKey: String,
        private val firstNameKey: String,
        private val lastNameKey: String
) : PrincipalExtractor, AuthoritiesExtractor {

    override fun extractPrincipal(props: MutableMap<String, Any>): ExternalPrincipal {
        val identity = props.getOrElse(idKey) { throw IllegalStateException("Could not learn the identity of the user") } as String
        return ExternalPrincipal(
                identity = identity,
                identityType = identityType,
                email = props.getOrElse(mailKey) { throw IllegalStateException("Bound account does not have e-mail address") } as String,
                firstName = props[firstNameKey] as String?,
                lastName = props[lastNameKey] as String?
        )
    }

    // every external user is just a user for us
    override fun extractAuthorities(props: MutableMap<String, Any>?): MutableList<GrantedAuthority> {
        return mutableListOf(SimpleGrantedAuthority("ROLE_USER"))
    }

}