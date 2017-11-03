package com.ggurgul.playground.extracker.auth.models

import java.security.Principal


data class ExternalPrincipal(
        val identityType: IdentityType,
        val identity: String,
        val email: String,
        val firstName : String?,
        val lastName : String?
) : Principal {

    override fun getName() = email

}