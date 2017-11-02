package com.ggurgul.playground.extracker.auth.controllers.resources

import org.hibernate.validator.constraints.Length
import java.io.Serializable
import javax.validation.constraints.NotNull

data class PasswordChangeRequest(
        @get:NotNull
        @get:Length(min = 6, max = 30)
        var oldPassword: String? = null,
        @get:NotNull
        @get:Length(min = 6, max = 30)
        var newPassword: String? = null
) : Serializable