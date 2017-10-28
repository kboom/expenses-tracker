package com.toptal.ggurgul.timezones.security.models

import java.io.Serializable

data class PasswordResetRequest(
        var email: String? = null
) : Serializable