package com.ggurgul.playground.extracker.auth.services

import com.ggurgul.playground.extracker.auth.models.ExternalPrincipal
import com.ggurgul.playground.extracker.auth.models.IdentityType
import org.springframework.boot.autoconfigure.security.oauth2.resource.AuthoritiesExtractor
import org.springframework.boot.autoconfigure.security.oauth2.resource.PrincipalExtractor
import org.springframework.security.core.GrantedAuthority
import java.security.Principal

class ExternalIdentityExtractor(
        private val identityType: IdentityType,
        private val idKey: String,
        private val mailKey: String,
        private val firstNameKey: String,
        private val lastNameKey: String,
        private val externalIdentityBinder: IdentityBinder
) : PrincipalExtractor, AuthoritiesExtractor {

    override fun extractPrincipal(props: MutableMap<String, Any>): Principal {
        return externalIdentityBinder.convertPrincipal(externalPrincipal(props))
    }

    override fun extractAuthorities(props: MutableMap<String, Any>): MutableList<GrantedAuthority> {
        return externalIdentityBinder.getAuthoritiesFrom(externalPrincipal(props)).toMutableList()
    }

    private fun externalPrincipal(props: MutableMap<String, Any>): ExternalPrincipal {
        val identity = props.getOrElse(idKey) { throw IllegalStateException("Could not learn the identity of the user") } as String
        return ExternalPrincipal(
                identity = identity,
                identityType = identityType,
                email = props.getOrElse(mailKey) { throw IllegalStateException("Bound account does not have e-mail address") } as String,
                firstName = props[firstNameKey] as String?,
                lastName = props[lastNameKey] as String?
        )
    }

}