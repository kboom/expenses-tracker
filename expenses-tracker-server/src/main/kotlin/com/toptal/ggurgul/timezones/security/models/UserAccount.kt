package com.toptal.ggurgul.timezones.security.models

import com.toptal.ggurgul.timezones.domain.models.security.User
import io.swagger.annotations.ApiModel
import io.swagger.annotations.ApiModelProperty
import org.hibernate.validator.constraints.Length
import springfox.documentation.annotations.ApiIgnore
import java.io.Serializable
import javax.validation.constraints.Null

@ApiModel(value = "Account", description = "A representation of User from the user perspective")
data class UserAccount(
        @get:Null
        @get:ApiModelProperty(readOnly = true)
        var username: String? = null,
        @get:Null
        @ApiModelProperty(readOnly = true)
        var email: String? = null,
        @get:Length(min = 2, max = 30, message = "First name length must be between 2 and 30 characters")
        var firstName: String? = null,
        @get:Length(min = 2, max = 40, message = "Last name length must be between 2 and 40 characters")
        var lastName: String? = null
) : Serializable {

    companion object {

        fun fromUser(user: User): UserAccount {
            return UserAccount(
                    username = user.username,
                    email = user.email,
                    firstName = user.firstName,
                    lastName = user.lastName
            )
        }

    }

}