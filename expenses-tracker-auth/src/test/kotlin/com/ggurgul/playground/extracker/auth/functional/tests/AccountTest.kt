package com.ggurgul.playground.extracker.auth.functional.tests

import com.ggurgul.playground.extracker.auth.functional.AbstractFunctionalTest
import com.ggurgul.playground.extracker.auth.management.LoginManager
import com.ggurgul.playground.extracker.auth.management.UsersManager
import io.restassured.RestAssured
import io.restassured.authentication.FormAuthConfig
import io.restassured.config.LogConfig
import org.hamcrest.Matchers
import org.junit.Test
import org.springframework.beans.factory.annotation.Autowired

// https://github.com/junit-team/junit4/wiki/Rules
class AccountTest : AbstractFunctionalTest() {

    @Autowired
    private lateinit var loginManager: LoginManager

    @Autowired
    private lateinit var userManager: UsersManager

    @Test
    fun userCanGetProfile() {
        val dummyUser = userManager.createDummyUser()
        RestAssured.given()
                .header("Authorization", "Bearer ${loginManager.getTokenFor(dummyUser.email, "secret")}")
                .get("/api/users/me")
                .then()
                .statusCode(200)
                .body("username", Matchers.equalTo("someone@anyone.com"))
    }

    @Test
    fun userCanGetProfileLocalLogin() {
        val dummyUser = userManager.createDummyUser()
        RestAssured.given()
                .auth().form(dummyUser.email, "secret", FormAuthConfig("api/login", "username", "password").withLoggingEnabled())
                .get("/api/users/me")
                .then()
                .statusCode(200)
                .body("username", Matchers.equalTo("someone@anyone.com"))
    }

}