package com.toptal.ggurgul.timezones.security.models

import io.swagger.annotations.ApiModel
import org.hibernate.validator.constraints.Length
import java.io.Serializable
import javax.validation.constraints.NotNull

@ApiModel(value = "AuthenticationRequest", description = "The details needed to authenticate user")
data class JwtAuthenticationRequest(
        @get:NotNull
        @get:Length(min = 4, max = 20)
        var username: String? = null,
        @get:NotNull
        @get:Length(min = 6, max = 30)
        var password: String? = null
) : Serializable
