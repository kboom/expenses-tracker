package com.toptal.ggurgul.timezones.security.permissions

import com.toptal.ggurgul.timezones.security.AuthenticatedUser
import java.io.Serializable

data class PermissionGrantRequest(
        val principal: AuthenticatedUser,
        val targetId: Serializable? = null,
        val targetObject: Any? = null,
        val permissionNeeded: Permission
)