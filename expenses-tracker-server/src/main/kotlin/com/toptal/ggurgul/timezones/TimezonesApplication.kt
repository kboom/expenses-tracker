package com.toptal.ggurgul.timezones

import com.toptal.ggurgul.timezones.domain.models.security.Authority
import org.springframework.beans.factory.annotation.Value
import org.springframework.boot.SpringApplication
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.context.annotation.Bean
import org.springframework.data.repository.query.spi.EvaluationContextExtension
import org.springframework.data.repository.query.spi.EvaluationContextExtensionSupport
import org.springframework.data.rest.core.config.RepositoryRestConfiguration
import org.springframework.data.rest.core.mapping.RepositoryDetectionStrategy.RepositoryDetectionStrategies.ANNOTATED
import org.springframework.data.rest.webmvc.config.RepositoryRestConfigurer
import org.springframework.data.rest.webmvc.config.RepositoryRestConfigurerAdapter
import org.springframework.mail.MailSender
import org.springframework.mail.SimpleMailMessage
import org.springframework.mail.javamail.JavaMailSenderImpl
import org.springframework.security.access.expression.SecurityExpressionRoot
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.web.cors.CorsConfiguration
import org.springframework.web.cors.UrlBasedCorsConfigurationSource
import org.springframework.web.filter.CorsFilter


@SpringBootApplication
open class TimezonesApplication {

    companion object {
        @JvmStatic
        fun main(args: Array<String>) {
            SpringApplication.run(TimezonesApplication::class.java, *args)
        }
    }

    @Bean
    open fun bCryptPasswordEncoder(): BCryptPasswordEncoder {
        return BCryptPasswordEncoder()
    }

    @Bean
    open fun securityExtension(): EvaluationContextExtension {
        return SecurityEvaluationContextExtension()
    }

    @Bean
    open fun repositoryRestConfigurer(): RepositoryRestConfigurer {

        return object : RepositoryRestConfigurerAdapter() {

            override fun configureRepositoryRestConfiguration(config: RepositoryRestConfiguration) {
                config.repositoryDetectionStrategy = ANNOTATED
                config.exposeIdsFor(Authority::class.java)
                config.isReturnBodyOnCreate = true
            }
        }

    }

    @Bean
    open fun mailSender(
            @Value("\${mail.host}") host: String,
            @Value("\${mail.account.username}") username: String,
            @Value("\${mail.account.password}") password: String
    ): MailSender {
        val sender = JavaMailSenderImpl()
        sender.host = host
        sender.username = username
        sender.password = password

        sender.testConnection()

        return sender
    }

    @Bean
    open fun registrationEmailMessage(
            @Value("\${registration.email.subject}") subject: String,
            @Value("\${registration.email.from}") from: String
    ): SimpleMailMessage {
        val message = SimpleMailMessage()
        message.from = from
        message.subject = subject
        return message
    }

    @Bean
    open fun passwordResetEmailMessage(
            @Value("\${user.password.reset.email.subject}") subject: String,
            @Value("\${user.password.reset.email.from}") from: String
    ): SimpleMailMessage {
        val message = SimpleMailMessage()
        message.from = from
        message.subject = subject
        return message
    }

    @Bean
    open fun corsFilter(): CorsFilter {
        val source = UrlBasedCorsConfigurationSource()
        val config = CorsConfiguration()
        config.allowCredentials = true
        config.addAllowedOrigin("*")
        config.addAllowedHeader("*")
        config.addAllowedMethod("OPTIONS")
        config.addAllowedMethod("GET")
        config.addAllowedMethod("POST")
        config.addAllowedMethod("PATCH")
        config.addAllowedMethod("PUT")
        config.addAllowedMethod("DELETE")
        source.registerCorsConfiguration("/**", config)
        return CorsFilter(source)
    }

    internal inner class SecurityEvaluationContextExtension : EvaluationContextExtensionSupport() {

        override fun getExtensionId(): String {
            return "security"
        }

        override fun getRootObject(): SecurityExpressionRoot {
            val authentication = SecurityContextHolder.getContext().authentication
            return object : SecurityExpressionRoot(authentication) {}
        }
    }

}
