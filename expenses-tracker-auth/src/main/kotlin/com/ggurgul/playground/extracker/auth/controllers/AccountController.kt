package com.ggurgul.playground.extracker.auth.controllers

import com.ggurgul.playground.extracker.auth.controllers.resources.PasswordChangeRequest
import com.ggurgul.playground.extracker.auth.controllers.resources.PasswordResetRequest
import com.ggurgul.playground.extracker.auth.controllers.resources.SetNewPasswordRequest
import com.ggurgul.playground.extracker.auth.management.SystemRunner
import com.ggurgul.playground.extracker.auth.services.UserService
import com.ggurgul.playground.extracker.auth.exceptions.UserNotFoundException
import com.ggurgul.playground.extracker.auth.models.UserAccount
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import javax.validation.ConstraintViolationException
import javax.validation.Valid

@RestController
@RequestMapping("/api/account")
class AccountController
@Autowired constructor(
        private val userService: UserService,
        private val systemRunner: SystemRunner
) {

    @RequestMapping(method = arrayOf(RequestMethod.GET))
    fun getProfile(): ResponseEntity<UserAccount> {
        return ResponseEntity.ok(UserAccount.fromUser(userService.getActingUser()));
    }

    @ResponseStatus(HttpStatus.NO_CONTENT)
    @RequestMapping(method = arrayOf(RequestMethod.PUT))
    fun putProfile(@RequestBody @Valid userAccount: UserAccount) {
        userService.updateProfile(userAccount)
    }

    @RequestMapping(value = "/password", method = arrayOf(RequestMethod.POST))
    @ResponseStatus(HttpStatus.NO_CONTENT)
    fun changePassword(
            @RequestBody @Valid passwordChangeRequest: PasswordChangeRequest) {
        val actingUser = userService.getActingUser();
        userService.changePasswordFor(actingUser,
                passwordChangeRequest.oldPassword!!, passwordChangeRequest.newPassword!!)
    }

    @RequestMapping(value = "/password/reset", method = arrayOf(RequestMethod.POST))
    @ResponseStatus(HttpStatus.NO_CONTENT)
    fun resetPassword(
            @RequestBody @Valid passwordResetRequest: PasswordResetRequest) {
        try {
            userService.resetPassword(passwordResetRequest.email!!)
        } catch (e: UserNotFoundException) {
            // we don't want to give hints about other emails
        }
    }

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
