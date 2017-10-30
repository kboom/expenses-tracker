package com.ggurgul.playground.extracker.auth

import org.springframework.beans.factory.annotation.Value
import org.springframework.boot.SpringApplication
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.data.repository.query.spi.EvaluationContextExtension
import org.springframework.data.repository.query.spi.EvaluationContextExtensionSupport
import org.springframework.http.MediaType
import org.springframework.mail.MailSender
import org.springframework.mail.SimpleMailMessage
import org.springframework.mail.javamail.JavaMailSenderImpl
import org.springframework.security.access.expression.SecurityExpressionRoot
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import org.springframework.web.cors.CorsConfiguration
import org.springframework.web.cors.UrlBasedCorsConfigurationSource
import org.springframework.web.filter.CorsFilter
import org.springframework.web.servlet.config.annotation.ContentNegotiationConfigurer
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter
import java.security.Principal

/**
 * Implement internal user mechanisms https://gigsterous.github.io/engineering/2017/03/01/spring-boot-4.html
 *
 * http://www.swisspush.org/security/2016/10/17/oauth2-in-depth-introduction-for-enterprises
 */
@SpringBootApplication
@RestController
class App {

    @RequestMapping(path = arrayOf("/user", "/me"), produces = arrayOf(MediaType.ALL_VALUE))
    fun user(principal: Principal): Principal {
        return principal
    }

    @Bean
    fun bCryptPasswordEncoder(): BCryptPasswordEncoder {
        return BCryptPasswordEncoder()
    }

    @Bean
    fun securityExtension(): EvaluationContextExtension {
        return SecurityEvaluationContextExtension()
    }

    @Bean
    fun mailSender(
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
    fun registrationEmailMessage(
            @Value("\${registration.email.subject}") subject: String,
            @Value("\${registration.email.from}") from: String
    ): SimpleMailMessage {
        val message = SimpleMailMessage()
        message.from = from
        message.subject = subject
        return message
    }

    @Bean
    fun passwordResetEmailMessage(
            @Value("\${user.password.reset.email.subject}") subject: String,
            @Value("\${user.password.reset.email.from}") from: String
    ): SimpleMailMessage {
        val message = SimpleMailMessage()
        message.from = from
        message.subject = subject
        return message
    }

    @Bean
    fun corsFilter(): CorsFilter {
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


    @Configuration
    class WebConfig : WebMvcConfigurerAdapter() {

        override fun configureContentNegotiation(configurer: ContentNegotiationConfigurer?) {
            configurer!!.favorPathExtension(false)
                    .favorParameter(true)
                    .defaultContentType(MediaType.APPLICATION_JSON)
                    .mediaType("xml", MediaType.APPLICATION_XML)
        }

    }

    companion object {

        @JvmStatic
        fun main(args: Array<String>) {
            SpringApplication.run(App::class.java, *args)
        }
    }

}

