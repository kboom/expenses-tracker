package com.ggurgul.playground.extracker.auth.rules

import com.ggurgul.playground.extracker.auth.data.TestUser

@Target(AnnotationTarget.FUNCTION)
annotation class AuthenticatedAsUser(
        val user: TestUser
)