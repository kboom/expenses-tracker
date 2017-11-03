package com.ggurgul.playground.extracker.auth.services

import com.ggurgul.playground.extracker.auth.models.User
import com.ggurgul.playground.extracker.auth.models.UserCode
import com.ggurgul.playground.extracker.auth.models.UserCodeType
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
        userCode.code = codeTranslator.generateFor(user.email!!)
        return userCode
    }


}