package com.ggurgul.playground.extracker.auth.functional.tests

import com.ggurgul.playground.extracker.auth.functional.AbstractFunctionalTest
import com.ggurgul.playground.extracker.auth.repositories.UserRepository
import io.restassured.RestAssured
import org.junit.Before
import org.junit.Test
import org.springframework.beans.factory.annotation.Autowired


class RegistrationTest : AbstractFunctionalTest() {

    @Autowired
    private lateinit var userRepository: UserRepository

    @Before
    fun setUp() {
        userRepository.deleteAll()
    }

    @Test
    fun userCanRegister() {
        RestAssured.given()
                .body("""
                    {
                        "username": "peter",
                        "password": "peterPan123",
                        "email": "gurgul.grzegorz@gmail.com"
                    }
                """.trim())
                .post("/registration")
                .then()
                .statusCode(200)
    }

}