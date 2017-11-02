package com.ggurgul.playground.extracker.auth.controllers.resources

import com.ggurgul.playground.extracker.auth.services.UserPrincipal
import org.springframework.stereotype.Component

@Component
class ResourceFactory {

    fun toUserResource(principal: UserPrincipal) = UserResource(
            username = principal.username,
            firstName = principal.getFirstName(),
            lastName = principal.getLastName()
    )

}