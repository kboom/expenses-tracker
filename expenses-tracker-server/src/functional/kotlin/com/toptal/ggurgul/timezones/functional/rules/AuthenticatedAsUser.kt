package com.toptal.ggurgul.timezones.functional.rules

import com.toptal.ggurgul.timezones.functional.database.User

@Target(AnnotationTarget.FUNCTION)
annotation class AuthenticatedAsUser(
        val user: User
)