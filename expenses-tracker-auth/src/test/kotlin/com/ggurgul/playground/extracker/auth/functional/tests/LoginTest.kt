package com.ggurgul.playground.extracker.auth.functional.tests


import com.ggurgul.playground.extracker.auth.functional.AbstractFunctionalTest
import com.ggurgul.playground.extracker.auth.management.UsersManager
import io.restassured.RestAssured.given
import io.restassured.http.Header
import io.restassured.response.Response
import org.junit.Before
import org.junit.Test
import org.springframework.beans.factory.annotation.Autowired
import java.util.*


class LoginTest : AbstractFunctionalTest() {

    @Autowired
    private lateinit var usersManager: UsersManager

    @Before
    fun setup() {
        usersManager.resetAll();
        usersManager.createDummyUser()
    }

    @Test
    @Throws(Exception::class)
    fun cannotGetTokenIfWrongPasswordUsed() {
        issueTokenRequest("dummy", "invalid").then().statusCode(400)
    }

    @Test
    @Throws(Exception::class)
    fun cannotGetTokenIfNotExistingUserUsed() {
        issueTokenRequest("invalid", "anything").then().statusCode(401)
    }

    @Test
    @Throws(Exception::class)
    fun canUseTokenToGetSecuredUserDetails() {
        val token = getToken(issueTokenRequest("dummy", "secret"))
        given()
                .header(Header("Authorization", "Bearer " + token))
                .get("/auth/account")
                .then()
                .statusCode(200)
    }

    @Test
    @Throws(Exception::class)
    fun cannotUseTemperedWithToken() {
        val token = getToken(issueTokenRequest("dummy", "secret"))
        given()
                .header(Header("Authorization", "Bearer " + token + "x"))
                .get("/auth/account")
                .then()
                .statusCode(401)
    }

    @Test
    @Throws(Exception::class)
    fun unauthorizedIfNoTokenPresent() {
        given()
                .get("/auth/account")
                .then()
                .statusCode(401)
    }

    @Throws(Exception::class)
    private fun issueTokenRequest(username: String, password: String): Response {
        val params = HashMap<String, String>()
        params.put("grant_type", "password")
        params.put("client_id", VALID_CLIENT_ID)
        params.put("username", username)
        params.put("password", password)

        return given()
                .contentType("application/x-www-form-urlencoded")
                .params(params)
                .auth()
                .preemptive()
                .basic(VALID_CLIENT_ID, VALID_CLIENT_SECRET)
                .post("/oauth/token")
    }

    private fun getToken(result: Response): String {
        result.then().statusCode(200)
        return result.jsonPath().getString("access_token")
    }

    companion object {
        private val VALID_CLIENT_ID = "expenses-tracker-service"
        private val VALID_CLIENT_SECRET = "expenses-tracker-service-secret"
    }

}