package com.toptal.ggurgul.timezones.domain.events

import com.toptal.ggurgul.timezones.domain.models.security.UserCode
import org.springframework.context.ApplicationEvent

class RegistrationCodeIssuedEvent(userCodes: UserCode) : ApplicationEvent(userCodes) {

    override fun getSource() = super.getSource() as UserCode

}