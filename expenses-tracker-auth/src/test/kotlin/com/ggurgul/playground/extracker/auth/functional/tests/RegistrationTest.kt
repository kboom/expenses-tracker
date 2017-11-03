package com.ggurgul.playground.extracker.auth.functional.tests

import com.ggurgul.playground.extracker.auth.functional.AbstractFunctionalTest
import com.ggurgul.playground.extracker.auth.models.User
import com.ggurgul.playground.extracker.auth.models.UserCodeType
import com.ggurgul.playground.extracker.auth.repositories.UserCodesRepository
import com.ggurgul.playground.extracker.auth.repositories.UserRepository
import com.ggurgul.playground.extracker.auth.services.UserService
import io.restassured.RestAssured
import org.junit.Before
import org.junit.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.security.crypto.password.PasswordEncoder


class RegistrationTest : AbstractFunctionalTest() {

    @Autowired
    private lateinit var userRepository: UserRepository

    @Autowired
    private lateinit var codesRepository: UserCodesRepository

    @Autowired
    private lateinit var passwordEncoder: PasswordEncoder

    @Autowired
    private lateinit var userService: UserService

    @Before
    fun setUp() {
        codesRepository.deleteAll()
        userRepository.deleteAll()
    }

    @Test
    fun userCanRegister() {
        RestAssured.given()
                .body("""
                    {
                        "email": "peter",
                        "password": "peterPan123",
                        "email": "gurgul.grzegorz@gmail.com"
                    }
                """.trim())
                .post("/registration")
                .then()
                .statusCode(200)
    }

    @Test
    fun isUnauthorizedForWrongConfirmationCode() {
        val registrationCode = createUserAndGetConfirmationCode()

        RestAssured.given()
                .body("""
                    {
                        "code": "${registrationCode}x"
                    }
                """.trim())
                .post("/registration/confirmation")
                .then()
                .statusCode(403)
    }

    @Test
    fun cannotUseSameConfirmationCodeTwice() {
        val registrationCode = createUserAndGetConfirmationCode()

        val confirmationRequest = RestAssured.given()
                .body("""
                    {
                        "code": "$registrationCode"
                    }
                """.trim())

        confirmationRequest
                .post("/registration/confirmation")
        confirmationRequest
                .post("/registration/confirmation")
                .then()
                .statusCode(403)
    }

    @Test
    fun accountIsActivatedForValidConfirmationCode() {
        val registrationCode = createUserAndGetConfirmationCode()

        RestAssured.given()
                .body("""
                    {
                        "code": "$registrationCode"
                    }
                """.trim())
                .post("/registration/confirmation")
                .then()
                .statusCode(200)
    }

    private fun createUserAndGetConfirmationCode(): String {
        val user = userService.registerUser(User(
                email = "someone@anyone.com",
                password = passwordEncoder.encode("qwerty")
        ))

        return codesRepository.findByUserAndType(user, UserCodeType.REGISTRATION_CONFIRMATION).map { it.code }.get()
    }

}