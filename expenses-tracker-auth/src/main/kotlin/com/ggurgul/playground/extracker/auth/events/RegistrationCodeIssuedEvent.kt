package com.toptal.ggurgul.timezones.domain.events

import com.ggurgul.playground.extracker.auth.models.UserCode
import org.springframework.context.ApplicationEvent

class RegistrationCodeIssuedEvent(userCodes: UserCode) : ApplicationEvent(userCodes) {

    override fun getSource() = super.getSource() as UserCode

}