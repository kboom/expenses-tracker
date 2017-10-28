package com.toptal.ggurgul.timezones.functional.tests.authentication

import com.toptal.ggurgul.timezones.functional.database.Authority
import com.toptal.ggurgul.timezones.functional.database.User.*
import com.toptal.ggurgul.timezones.functional.database.assignAuthorityToUser
import com.toptal.ggurgul.timezones.functional.database.insertUser
import com.toptal.ggurgul.timezones.functional.rules.AuthenticationRule
import com.toptal.ggurgul.timezones.functional.rules.DataLoadingRule
import com.toptal.ggurgul.timezones.functional.rules.ReadOnly
import com.toptal.ggurgul.timezones.functional.tests.AbstractFunctionalTest
import io.restassured.RestAssured.given
import org.hamcrest.Matchers.isA
import org.junit.Rule
import org.junit.Test
import org.junit.rules.RuleChain
import org.junit.rules.TestRule

internal class AuthenticationTests : AbstractFunctionalTest() {

    private val authenticationRule = AuthenticationRule()
    private val dataLoadingRule = DataLoadingRule({
        insertInto("USERS") {
            insertUser(this, GREG)
            insertUser(this, AGATHA)
            insertUser(this, ALICE)
            insertUser(this, KATE)
        }
        insertInto("USER_AUTHORITIES") {
            assignAuthorityToUser(this, Authority.ADMIN, GREG)
            assignAuthorityToUser(this, Authority.MANAGER, AGATHA)
            assignAuthorityToUser(this, Authority.USER, ALICE)
            assignAuthorityToUser(this, Authority.USER, KATE)
        }
    })

    @get:Rule
    var chain: TestRule = RuleChain.outerRule(dataLoadingRule)
            .around(authenticationRule);

    @Test
    @ReadOnly
    fun userCannotObtainTokenIfNotEnabled() {
        given()
                .body("""
                    {
                        "username": "${KATE.username}",
                        "password": "${KATE.password}"
                    }
                """.trim())
                .post("/auth")
                .then()
                .statusCode(401)
    }

    @Test
    @ReadOnly
    fun userCannotObtainTokenIfInvalidUsername() {
        given()
                .body("""
                    {
                        "username": "invalid",
                        "password": "qwerty123"
                    }
                """.trim())
                .post("/auth")
                .then()
                .statusCode(401)
    }

    @Test
    @ReadOnly
    fun userCannotObtainTokenIfInvalidPassword() {
        given()
                .body("""
                    {
                        "username": "greg",
                        "password": "invalid"
                    }
                """.trim())
                .post("/auth")
                .then()
                .statusCode(401)
    }

    @Test
    @ReadOnly
    fun userCanObtainUserToken() {
        given()
                .body("""
                    {
                        "username": "${ALICE.username}",
                        "password": "${ALICE.password}"
                    }
                """.trim())
                .post("/auth")
                .then()
                .statusCode(200)
                .and()
                .body("token", isA(String::class.java))
    }

    @Test
    @ReadOnly
    fun managerCanObtainManagerToken() {
        given()
                .body("""
                    {
                        "username": "${AGATHA.username}",
                        "password": "${AGATHA.password}"
                    }
                """.trim())
                .post("/auth")
                .then()
                .statusCode(200)
                .and()
                .body("token", isA(String::class.java))
    }

    @Test
    @ReadOnly
    fun adminCanObtainAdminToken() {
        given()
                .body("""
                    {
                        "username": "${GREG.username}",
                        "password": "${GREG.password}"
                    }
                """.trim())
                .post("/auth")
                .then()
                .statusCode(200)
                .and()
                .body("token", isA(String::class.java))
    }

}