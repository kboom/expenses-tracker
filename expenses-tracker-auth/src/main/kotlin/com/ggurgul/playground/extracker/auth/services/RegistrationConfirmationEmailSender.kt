package com.toptal.ggurgul.timezones.domain.services

import com.toptal.ggurgul.timezones.domain.events.RegistrationCodeIssuedEvent
import com.ggurgul.playground.extracker.auth.models.UserCode
import org.apache.commons.logging.LogFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.event.EventListener
import org.springframework.mail.MailException
import org.springframework.mail.MailSender
import org.springframework.mail.SimpleMailMessage
import org.springframework.stereotype.Component


@Component
open class RegistrationConfirmationEmailSender
@Autowired constructor(
        @Value("\${registration.confirmation.link}") private val confirmationLink: String,
        private val mailSender: MailSender,
        @Qualifier("registrationEmailMessage") private val templateMessage: SimpleMailMessage
) {

    companion object {
        private val logger = LogFactory.getLog(RegistrationConfirmationEmailSender::class.java)
    }

    @EventListener
    fun sendRegistrationMessage(event: RegistrationCodeIssuedEvent) {
        val msg = SimpleMailMessage(this.templateMessage)
        val userCode = event.source

        msg.setTo(userCode.sentToEmail)

        msg.text = """
            Welcome to TimeZone Demo Application!
            If it is you who registered please follow the link ${getConfirmationLink(userCode)} to end the registration process!
        """.trimIndent()

        try {
            this.mailSender.send(msg)
        } catch (ex: MailException) {
            logger.error("Could not send registration confirmation e-mail", ex)
        }

    }

    private fun getConfirmationLink(userCode: UserCode): String {
        return confirmationLink.replace("##code##", userCode.code!!, true)
    }


}