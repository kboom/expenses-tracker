package com.ggurgul.playground.extracker.auth.functional.tests

import com.ggurgul.playground.extracker.auth.functional.AbstractFunctionalTest
import io.restassured.RestAssured
import org.junit.Test


class RegistrationTest : AbstractFunctionalTest() {

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
                .statusCode(201)
    }

}