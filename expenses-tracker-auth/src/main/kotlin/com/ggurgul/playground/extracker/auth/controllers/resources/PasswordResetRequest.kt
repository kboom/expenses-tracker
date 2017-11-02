package com.ggurgul.playground.extracker.auth.controllers.resources

import java.io.Serializable

data class PasswordResetRequest(
        var email: String? = null
) : Serializable