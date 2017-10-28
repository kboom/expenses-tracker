package com.toptal.ggurgul.timezones.security.models

import java.io.Serializable

data class RegistrationRequest(
        var username: String? = null,
        var password: String? = null,
        var email: String? = null
) : Serializable