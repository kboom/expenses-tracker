package com.toptal.ggurgul.timezones.domain.services

import com.toptal.ggurgul.timezones.domain.models.security.User
import com.toptal.ggurgul.timezones.domain.models.security.UserCode
import com.toptal.ggurgul.timezones.domain.models.security.UserCodeType
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component


@Component
class UserCodeFactory
@Autowired constructor(private val codeTranslator: UserCodeTranslator) {

    fun generateFor(user: User, codeType: UserCodeType): UserCode {
        val userCode = UserCode()
        userCode.user = user
        userCode.type = codeType
        userCode.sentToEmail = user.email
        userCode.code = codeTranslator.generateFor(user.username!!)
        return userCode
    }


}