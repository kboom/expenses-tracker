package com.toptal.ggurgul.timezones.functional.database

import com.ninja_squad.dbsetup.operation.Insert
import com.ninja_squad.dbsetup_kotlin.mappedValues


enum class Authority(
        val roleName: String
) {

    ADMIN(roleName = "ROLE_ADMIN"),
    MANAGER(roleName = "ROLE_MANAGER"),
    USER(roleName = "ROLE_USER")

}


fun assignAuthorityToUser(insertBuilder: Insert.Builder, authority: Authority, user: User) {
    insertBuilder.mappedValues(
            "USER_ID" to user.id,
            "AUTHORITY_NAME" to authority.roleName
    )
}

