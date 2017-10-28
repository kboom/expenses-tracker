package com.toptal.ggurgul.timezones.functional.database

import com.ninja_squad.dbsetup.generator.ValueGenerators
import com.ninja_squad.dbsetup.operation.Insert
import com.ninja_squad.dbsetup_kotlin.mappedValues
import com.toptal.ggurgul.timezones.domain.models.security.UserCodeType

fun insertRegistrationConfirmationCode(insertBuilder: Insert.Builder, user: User, code: String) {
    insertConfirmationCode(insertBuilder, user, code, UserCodeType.REGISTRATION_CONFIRMATION)
}


fun insertPasswordResetConfirmationCode(insertBuilder: Insert.Builder, user: User, code: String) {
    insertConfirmationCode(insertBuilder, user, code, UserCodeType.PASSWORD_RESET)
}

private fun insertConfirmationCode(insertBuilder: Insert.Builder, user: User, code: String, codeType: UserCodeType) {
    insertBuilder.withGeneratedValue("ID", ValueGenerators.sequence()
            .startingAt(1000L).incrementingBy(10))
    insertBuilder.mappedValues(
            "USER_ID" to user.id,
            "SENT_TO" to user.email,
            "TYPE" to codeType,
            "USER_ID" to user.id,
            "CODE" to code
    )
}