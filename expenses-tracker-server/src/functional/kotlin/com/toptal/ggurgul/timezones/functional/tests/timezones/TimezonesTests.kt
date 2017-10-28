package com.toptal.ggurgul.timezones.functional.tests.timezones

import com.toptal.ggurgul.timezones.functional.database.*
import com.toptal.ggurgul.timezones.functional.database.User.*
import com.toptal.ggurgul.timezones.functional.rules.AuthenticatedAsUser
import com.toptal.ggurgul.timezones.functional.rules.AuthenticationRule
import com.toptal.ggurgul.timezones.functional.rules.DataLoadingRule
import com.toptal.ggurgul.timezones.functional.rules.ReadOnly
import com.toptal.ggurgul.timezones.functional.tests.AbstractFunctionalTest
import io.restassured.RestAssured.given
import org.hamcrest.Matchers.*
import org.junit.Rule
import org.junit.Test
import org.junit.rules.RuleChain
import org.junit.rules.TestRule

internal class TimezonesTests : AbstractFunctionalTest() {

    private val authenticationRule = AuthenticationRule()
    private val dataLoadingRule = DataLoadingRule({
        insertInto("USERS") {
            insertUser(this, GREG)
            insertUser(this, AGATHA)
            insertUser(this, ALICE)
        }
        insertInto("USER_AUTHORITIES") {
            assignAuthorityToUser(this, Authority.ADMIN, GREG)
            assignAuthorityToUser(this, Authority.MANAGER, AGATHA)
            assignAuthorityToUser(this, Authority.USER, ALICE)
        }
        insertInto("TIMEZONES") {
            insertTimezone(this, Timezone.KRAKOW, GREG)
            insertTimezone(this, Timezone.TOKYO, GREG)
            insertTimezone(this, Timezone.WARSAW, AGATHA)
            insertTimezone(this, Timezone.SYDNEY, ALICE)
        }
    })

    @get:Rule
    var chain: TestRule = RuleChain.outerRule(dataLoadingRule)
            .around(authenticationRule);

    @Test
    @ReadOnly
    @AuthenticatedAsUser(ALICE)
    fun userSeesHisOwnTimezones() {
        given()
                .header("Authorization", authenticationRule.token)
                .get("/timezones")
                .then()
                .statusCode(200)
                .body("_embedded.timezones", hasSize<String>(equalTo(1)))
                .body("_embedded.timezones[0].name", equalTo("My Sydney"))
    }

    @Test
    @ReadOnly
    @AuthenticatedAsUser(AGATHA)
    fun managerSeesHisOwnTimezones() {
        given()
                .header("Authorization", authenticationRule.token)
                .get("/timezones")
                .then()
                .statusCode(200)
                .body("_embedded.timezones", hasSize<String>(equalTo(1)))
                .body("_embedded.timezones[0].name", equalTo("My Warsaw"))
    }

    @Test
    @ReadOnly
    @AuthenticatedAsUser(GREG)
    fun adminSeesAllTimezones() {
        given()
                .header("Authorization", authenticationRule.token)
                .get("/timezones?projection=withDetails")
                .then()
                .statusCode(200)
                .body("_embedded.timezones", hasSize<String>(equalTo(4)))
                .body("_embedded.timezones.owner", containsInAnyOrder<String>("greg", "greg", "agatha", "alice"))
    }

    @Test
    @AuthenticatedAsUser(ALICE)
    fun userCanCreateTimezone() {
        given()
                .header("Authorization", authenticationRule.token)
                .body("""{
                    "name": "Middle of nowhere",
                    "locationName": "Nowhere",
                    "differenceToGMT": 12
                }""".trimIndent())
                .post("/timezones")
                .then()
                .statusCode(201)
    }

    @Test
    @AuthenticatedAsUser(ALICE)
    fun userCanUpdateTimezone() {
        given()
                .header("Authorization", authenticationRule.token)
                .body("""{
                    "name": "Right here",
                    "locationName": "Here",
                    "differenceToGMT": 0
                }""".trimIndent())
                .put("/timezones/${Timezone.SYDNEY.id}")
                .then()
                .statusCode(200)
    }

    @Test
    @AuthenticatedAsUser(ALICE)
    fun userCanDeleteTimezone() {
        given()
                .header("Authorization", authenticationRule.token)
                .delete("/timezones/${Timezone.SYDNEY.id}")
                .then()
                .statusCode(204)
    }

    @Test
    @ReadOnly
    @AuthenticatedAsUser(ALICE)
    fun userCannotSeeAnotherUserTimezone() {
        given()
                .header("Authorization", authenticationRule.token)
                .get("/timezones/${Timezone.KRAKOW.id}")
                .then()
                .statusCode(403)
    }


    @Test
    @ReadOnly
    @AuthenticatedAsUser(ALICE)
    fun userCannotDeleteAnotherUserTimezone() {
        given()
                .header("Authorization", authenticationRule.token)
                .delete("/timezones/${Timezone.KRAKOW.id}")
                .then()
                .statusCode(403)
    }

    @Test
    @ReadOnly
    @AuthenticatedAsUser(AGATHA)
    fun managerCannotSeeAnotherUserTimezone() {
        given()
                .header("Authorization", authenticationRule.token)
                .get("/timezones/${Timezone.SYDNEY.id}")
                .then()
                .statusCode(403)
    }

    @Test
    @ReadOnly
    @AuthenticatedAsUser(AGATHA)
    fun managerCannotDeleteAnotherUserTimezone() {
        given()
                .header("Authorization", authenticationRule.token)
                .delete("/timezones/${Timezone.SYDNEY.id}")
                .then()
                .statusCode(403)
    }

    @Test
    @AuthenticatedAsUser(AGATHA)
    fun deletingUserDeletesHisTimezones() {
        given()
                .header("Authorization", authenticationRule.token)
                .delete("/users/${ALICE.id}")
                .then()
                .statusCode(204)

        given()
                .header("Authorization", authenticationRule.token)
                .get("/timezones?projection=withDetails")
                .then()
                .body("_embedded.timezones.owner", not(contains("alice")))
    }

}