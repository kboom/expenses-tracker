package com.toptal.ggurgul.timezones.controllers

import com.toptal.ggurgul.timezones.domain.services.UserService
import com.toptal.ggurgul.timezones.exceptions.UserNotFoundException
import com.toptal.ggurgul.timezones.exceptions.WrongConfirmationCodeException
import com.toptal.ggurgul.timezones.security.SystemRunner
import com.toptal.ggurgul.timezones.security.models.PasswordChangeRequest
import com.toptal.ggurgul.timezones.security.models.PasswordResetRequest
import com.toptal.ggurgul.timezones.security.models.SetNewPasswordRequest
import com.toptal.ggurgul.timezones.security.models.UserAccount
import io.swagger.annotations.Api
import io.swagger.annotations.ApiOperation
import org.hibernate.validator.internal.engine.ConstraintViolationImpl
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.security.core.AuthenticationException
import org.springframework.web.bind.annotation.*
import javax.validation.ConstraintViolationException
import javax.validation.Valid

@RestController
@Api(value = "account", description = "Account operations", tags = arrayOf("account"))
@RequestMapping("/account")
class AccountRestController
@Autowired constructor(
        private val userService: UserService,
        private val systemRunner: SystemRunner
) {

    @ApiOperation(value = "Get profile", response = UserAccount::class)
    @RequestMapping(method = arrayOf(RequestMethod.GET))
    fun getProfile(): ResponseEntity<UserAccount> {
        return ResponseEntity.ok(UserAccount.fromUser(userService.getActingUser()));
    }

    @ApiOperation(value = "Update account")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @RequestMapping(method = arrayOf(RequestMethod.PUT))
    fun putProfile(@RequestBody @Valid userAccount: UserAccount) {
        userService.updateProfile(userAccount)
    }

    @ApiOperation(value = "Change password")
    @RequestMapping(value = "/password", method = arrayOf(RequestMethod.POST))
    @ResponseStatus(HttpStatus.NO_CONTENT)
    fun changePassword(
            @RequestBody @Valid passwordChangeRequest: PasswordChangeRequest) {
        val actingUser = userService.getActingUser();
        userService.changePasswordFor(actingUser,
                passwordChangeRequest.oldPassword!!, passwordChangeRequest.newPassword!!)
    }

    @ApiOperation(value = "Reset password")
    @RequestMapping(value = "/password/reset", method = arrayOf(RequestMethod.POST))
    @ResponseStatus(HttpStatus.NO_CONTENT)
    fun resetPassword(
            @RequestBody @Valid passwordResetRequest: PasswordResetRequest) {
        try {
            userService.resetPassword(passwordResetRequest.email!!)
        } catch(e: UserNotFoundException) {
            // we don't want to give hints about other emails
        }
    }

    @ApiOperation(value = "Set new password")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @RequestMapping(value = "/password/reset/confirmation", method = arrayOf(RequestMethod.POST))
    fun confirmPasswordReset(
            @RequestBody @Valid passwordResetRequest: SetNewPasswordRequest) {
        systemRunner.runInSystemContext {
            userService.confirmResetPassword(passwordResetRequest.code!!, passwordResetRequest.newPassword!!)
        }
    }

    @ExceptionHandler(ConstraintViolationException::class)
    fun handleConstraintViolation(e: ConstraintViolationException) {
        System.out.println("xyz");
    }

}
