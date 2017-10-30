package com.ggurgul.playground.extracker.auth.controllers.dto

import java.io.Serializable

data class SetNewPasswordRequest(
        var newPassword: String? = null,
        var code: String? = null
) : Serializable