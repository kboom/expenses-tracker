package com.ggurgul.playground.extracker.auth.services

import com.toptal.ggurgul.timezones.domain.events.PasswordResetCodeIssued
import com.ggurgul.playground.extracker.auth.models.UserCode
import com.toptal.ggurgul.timezones.domain.services.RegistrationConfirmationEmailSender
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
open class PasswordResetEmailSender
@Autowired constructor(
        @Value("\${user.password.reset.email.link}") private val passwordResetLink: String,
        private val mailSender: MailSender,
        @Qualifier("passwordResetEmailMessage") private val templateMessage: SimpleMailMessage
) {

    companion object {
        private val logger = LogFactory.getLog(RegistrationConfirmationEmailSender::class.java)
    }

    @EventListener
    fun sendPasswordResetMessage(event: PasswordResetCodeIssued) {
        val msg = SimpleMailMessage(this.templateMessage)
        val userCode = event.source

        msg.setTo(userCode.sentToEmail)

        msg.text = """
            Did you request password reset?
            If you did please follow the link ${getConfirmationLink(userCode)} to end the process!
            If not, please disregard this message.
        """.trimIndent()

        try {
            this.mailSender.send(msg)
        } catch (ex: MailException) {
            logger.error("Could not send password reset e-mail", ex)
        }

    }

    private fun getConfirmationLink(userCode: UserCode): String {
        return passwordResetLink.replace("##code##", userCode.code!!, true)
    }


}