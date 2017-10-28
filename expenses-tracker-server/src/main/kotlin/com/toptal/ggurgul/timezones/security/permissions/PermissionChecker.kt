package com.toptal.ggurgul.timezones.security.permissions

interface PermissionChecker {

    fun shouldGrantPermissionFor(grantRequest: PermissionGrantRequest): Boolean

    fun canHandle(grantRequest: PermissionGrantRequest): Boolean

}