package com.ggurgul.playground.extracker.auth.functional.tests

import com.ggurgul.playground.extracker.auth.functional.AbstractFunctionalTest
import com.ggurgul.playground.extracker.auth.management.LoginManager
import com.ggurgul.playground.extracker.auth.management.UsersManager
import io.restassured.RestAssured
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
                .header("Authorization", loginManager.getTokenFor(dummyUser.username!!, "secret"))
                .get("/account")
                .then()
                .statusCode(200)
                .body("username", Matchers.equalTo("alice"))
                .body("email", Matchers.equalTo("alice@test.com"))
                .body("firstName", Matchers.equalTo("Alice"))
                .body("lastName", Matchers.equalTo("Smith"))
    }

}