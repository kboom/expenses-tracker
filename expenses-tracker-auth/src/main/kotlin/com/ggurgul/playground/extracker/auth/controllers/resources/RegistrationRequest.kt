package com.ggurgul.playground.extracker.auth.controllers.resources

import java.io.Serializable

data class RegistrationRequest(
        var email: String? = null,
        var password: String? = null
) : Serializable