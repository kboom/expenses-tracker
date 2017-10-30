package com.ggurgul.playground.extracker.auth.functional.tests

import com.ggurgul.playground.extracker.auth.data.TestUser.GREEN_USER
import com.ggurgul.playground.extracker.auth.functional.AbstractFunctionalTest
import com.ggurgul.playground.extracker.auth.rules.AuthenticatedAsUser
import com.ggurgul.playground.extracker.auth.rules.AuthenticationRule
import io.restassured.RestAssured
import org.hamcrest.Matchers
import org.junit.Rule
import org.junit.Test
import org.springframework.boot.context.embedded.LocalServerPort


class AccountTest : AbstractFunctionalTest() {

    @LocalServerPort
    private var appPort: Int? = null

    @get:Rule
    var authenticationRule = object: AuthenticationRule() {

        override fun getPort() = appPort!!

    }

    @Test
    @AuthenticatedAsUser(GREEN_USER)
    fun userCanGetProfile() {
        RestAssured.given()
                .header("Authorization", authenticationRule.token)
                .get("/account")
                .then()
                .statusCode(200)
                .body("username", Matchers.equalTo("alice"))
                .body("email", Matchers.equalTo("alice@test.com"))
                .body("firstName", Matchers.equalTo("Alice"))
                .body("lastName", Matchers.equalTo("Smith"))
    }

}