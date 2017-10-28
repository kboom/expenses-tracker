package com.toptal.ggurgul.timezones.functional.tests.registration

import com.toptal.ggurgul.timezones.functional.database.*
import com.toptal.ggurgul.timezones.functional.rules.AuthenticationRule
import com.toptal.ggurgul.timezones.functional.rules.DataLoadingRule
import com.toptal.ggurgul.timezones.functional.rules.ReadOnly
import com.toptal.ggurgul.timezones.functional.tests.AbstractFunctionalTest
import io.restassured.RestAssured
import org.junit.Rule
import org.junit.Test
import org.junit.rules.RuleChain
import org.junit.rules.TestRule


class RegistrationTests : AbstractFunctionalTest() {

    companion object {
        val CONFIRMATION_CODE_FOR_KATE = "a2F0ZTo2ZTVmMjJkZTQ5YzMzOTkwMDEyM2IyMjlmYTAwNjAwNQ=="
    }

    private val authenticationRule = AuthenticationRule()
    private val dataLoadingRule = DataLoadingRule({
        insertInto("USERS") {
            insertUser(this, User.KATE)
        }
        insertInto("USER_AUTHORITIES") {
            assignAuthorityToUser(this, Authority.USER, User.KATE)
        }
        insertInto("USER_CODES") {
            insertRegistrationConfirmationCode(this, User.KATE, CONFIRMATION_CODE_FOR_KATE)
        }
    })

    @get:Rule
    var chain: TestRule = RuleChain.outerRule(dataLoadingRule)
            .around(authenticationRule);

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

    @Test
    @ReadOnly
    fun isUnauthorizedForWrongConfirmationCode() {
        RestAssured.given()
                .body("""
                    {
                        "code": "wrong"
                    }
                """.trim())
                .post("/registration/confirmation")
                .then()
                .statusCode(403)
    }

    @Test
    @ReadOnly
    fun cannotUseSameConfirmationCodeTwice() {
        val confirmationRequest = RestAssured.given()
                .body("""
                    {
                        "code": "$CONFIRMATION_CODE_FOR_KATE"
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
        RestAssured.given()
                .body("""
                    {
                        "code": "$CONFIRMATION_CODE_FOR_KATE"
                    }
                """.trim())
                .post("/registration/confirmation")
                .then()
                .statusCode(200)
    }

}