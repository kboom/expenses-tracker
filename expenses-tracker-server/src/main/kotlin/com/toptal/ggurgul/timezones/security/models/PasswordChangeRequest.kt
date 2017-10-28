package com.toptal.ggurgul.timezones.security.models

import io.swagger.annotations.ApiModel
import org.hibernate.validator.constraints.Length
import java.io.Serializable
import javax.validation.constraints.NotNull

@ApiModel(value = "PasswordChangeRequest", description = "The details needed to change password")
data class PasswordChangeRequest(
        @get:NotNull
        @get:Length(min = 6, max = 30)
        var oldPassword: String? = null,
        @get:NotNull
        @get:Length(min = 6, max = 30)
        var newPassword: String? = null
) : Serializable