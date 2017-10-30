package com.ggurgul.playground.extracker.auth.exceptions


data class ApplicationErrorMessage(
        var code: Int? = null,
        var message: String? = null
)