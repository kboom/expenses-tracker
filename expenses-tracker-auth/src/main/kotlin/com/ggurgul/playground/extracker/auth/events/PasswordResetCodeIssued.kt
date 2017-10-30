package com.toptal.ggurgul.timezones.domain.events

import com.ggurgul.playground.extracker.auth.models.UserCode
import org.springframework.context.ApplicationEvent

class PasswordResetCodeIssued(userCode: UserCode) : ApplicationEvent(userCode) {

    override fun getSource() = super.getSource() as UserCode

}