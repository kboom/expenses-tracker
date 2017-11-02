package com.ggurgul.playground.extracker.auth.controllers.resources

import org.springframework.hateoas.ResourceSupport


data class UserResource(
        val username: String,
        val firstName: String?,
        val lastName: String?
) : ResourceSupport()