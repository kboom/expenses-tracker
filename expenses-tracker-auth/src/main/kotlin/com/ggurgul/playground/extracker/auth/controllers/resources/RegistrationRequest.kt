package com.ggurgul.playground.extracker.auth.controllers.resources

import java.io.Serializable

data class RegistrationRequest(
        var username: String? = null,
        var password: String? = null,
        var email: String? = null
) : Serializable