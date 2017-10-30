package com.ggurgul.playground.extracker.auth.controllers.dto

import java.io.Serializable

data class PasswordResetRequest(
        var email: String? = null
) : Serializable