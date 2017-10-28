package com.toptal.ggurgul.timezones.security.permissions

enum class Permission(
        val permissionName: String
) {

    CREATE_TIMEZONE("timezone:create"),
    VIEW_TIMEZONE("timezone:view"),
    EDIT_TIMEZONE("timezone:edit"),
    DELETE_TIMEZONE("timezone:delete");

    companion object {
        fun permissionOf(permissionName: String) = values().find { it.permissionName == permissionName }!!
    }

}