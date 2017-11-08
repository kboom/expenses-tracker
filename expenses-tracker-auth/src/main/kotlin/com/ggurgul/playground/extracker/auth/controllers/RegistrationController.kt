package com.ggurgul.playground.extracker.auth.controllers

import com.ggurgul.playground.extracker.auth.controllers.resources.RegistrationConfirmationRequest
import com.ggurgul.playground.extracker.auth.controllers.resources.RegistrationRequest
import com.ggurgul.playground.extracker.auth.management.SystemRunner
import com.ggurgul.playground.extracker.auth.models.AuthorityName
import com.ggurgul.playground.extracker.auth.models.User
import com.ggurgul.playground.extracker.auth.repositories.AuthorityRepository
import com.ggurgul.playground.extracker.auth.services.UserService
import com.toptal.ggurgul.timezones.exceptions.RegistrationException
import org.apache.commons.logging.LogFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.ResponseEntity
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestMethod
import org.springframework.web.bind.annotation.RestController


@RestController
@RequestMapping(value = "/api/registration")
class RegistrationController
@Autowired
constructor(
        private val systemRunner: SystemRunner,
        private val passwordEncoder: BCryptPasswordEncoder,
        private val userService: UserService,
        private val authorityRepository: AuthorityRepository
) {

    companion object {
        private val LOG = LogFactory.getLog(RegistrationController::class.java)
    }

    @RequestMapping(method = arrayOf(RequestMethod.POST))
    @Throws(RegistrationException::class)
    fun registerUser(
            @RequestBody registrationRequest: RegistrationRequest
    ): ResponseEntity<*> {
        systemRunner.runInSystemContext {
            userService.registerUser(User(
                    email = registrationRequest.email!!,
                    password = passwordEncoder.encode(registrationRequest.password),
                    authorities = mutableListOf(authorityRepository.findOne(AuthorityName.ROLE_USER))
            ))
        }
        return ResponseEntity.ok("Registered")
    }

    @RequestMapping(value = "/confirmation", method = arrayOf(RequestMethod.POST))
    @Throws(RegistrationException::class)
    fun confirmUserRegistration(
            @RequestBody registrationConfirmationRequest: RegistrationConfirmationRequest
    ): ResponseEntity<Void> {
        return try {
            systemRunner.runInSystemContext {
                userService.activateUser(registrationConfirmationRequest.code)
            }
            ResponseEntity.ok().build()
        } catch (e: Exception) {
            LOG.debug("Could not activate user", e)
            respondWithUnauthorized()
        }
    }

    private fun respondWithUnauthorized(): ResponseEntity<Void> =
            ResponseEntity.status(403).build()

}