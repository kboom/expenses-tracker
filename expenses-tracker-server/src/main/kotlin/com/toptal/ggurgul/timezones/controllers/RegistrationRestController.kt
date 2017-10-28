package com.toptal.ggurgul.timezones.controllers

import com.toptal.ggurgul.timezones.domain.models.security.AuthorityName
import com.toptal.ggurgul.timezones.domain.models.security.User
import com.toptal.ggurgul.timezones.domain.repository.AuthorityRepository
import com.toptal.ggurgul.timezones.domain.repository.UserCodesRepository
import com.toptal.ggurgul.timezones.domain.repository.UserRepository
import com.toptal.ggurgul.timezones.domain.repository.handlers.TimezoneEventHandler
import com.toptal.ggurgul.timezones.domain.services.UserService
import com.toptal.ggurgul.timezones.exceptions.RegistrationException
import com.toptal.ggurgul.timezones.security.SystemRunner
import com.toptal.ggurgul.timezones.security.models.RegistrationConfirmationRequest
import com.toptal.ggurgul.timezones.security.models.RegistrationRequest
import io.swagger.annotations.Api
import io.swagger.annotations.ApiOperation
import io.swagger.annotations.ApiResponse
import io.swagger.annotations.ApiResponses
import org.apache.commons.logging.LogFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.ResponseEntity
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestMethod
import org.springframework.web.bind.annotation.RestController


@RestController
@Api(value = "registration", description = "Registration operations", tags = arrayOf("registration"))
@RequestMapping(value = "/registration")
class RegistrationRestController
@Autowired
constructor(
        private val systemRunner: SystemRunner,
        private val passwordEncoder: BCryptPasswordEncoder,
        private val userService: UserService,
        private val authorityRepository: AuthorityRepository
) {

    companion object {
        private val LOG = LogFactory.getLog(RegistrationRestController::class.java)
    }

    @ApiOperation(value = "Register account")
    @ApiResponses(
            ApiResponse(code = 200, message = "Registration successful"),
            ApiResponse(code = 500, message = "Registration failure")
    )
    @RequestMapping(method = arrayOf(RequestMethod.POST))
    @Throws(RegistrationException::class)
    fun registerUser(
            @RequestBody registrationRequest: RegistrationRequest
    ): ResponseEntity<*> {
        systemRunner.runInSystemContext {
            userService.registerUser(User().apply {
                username = registrationRequest.username
                password = passwordEncoder.encode(registrationRequest.password)
                email = registrationRequest.email
                authorities = listOf(authorityRepository.findOne(AuthorityName.ROLE_USER))
            })
        }
        return ResponseEntity.ok("Registered")
    }

    @ApiOperation(value = "Confirm account")
    @ApiResponses(
            ApiResponse(code = 200, message = "Confirmation successful"),
            ApiResponse(code = 500, message = "Confirmation failure")
    )
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