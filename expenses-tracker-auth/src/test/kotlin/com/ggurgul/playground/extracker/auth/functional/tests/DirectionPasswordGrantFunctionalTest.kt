package com.ggurgul.playground.extracker.auth.functional.tests


import com.ggurgul.playground.extracker.auth.functional.AbstractFunctionalTest
import com.ggurgul.playground.extracker.auth.models.User
import com.ggurgul.playground.extracker.auth.repositories.UserRepository
import io.restassured.RestAssured.given
import io.restassured.http.Header
import io.restassured.response.Response
import org.junit.Before
import org.junit.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.security.crypto.password.PasswordEncoder
import java.util.*


class DirectionPasswordGrantFunctionalTest : AbstractFunctionalTest() {

    @Autowired
    private lateinit var userRepository: UserRepository

    @Autowired
    private lateinit var passwordEncoder: PasswordEncoder

    @Before
    fun setup() {
        userRepository.deleteAll()
        userRepository.save(User(
                username = VALID_USER,
                password = passwordEncoder.encode(VALID_USER_PASS),
                email = "someone@anything.com",
                enabled = true
        ))
    }

    @Test
    @Throws(Exception::class)
    fun cannotGetTokenIfWrongPasswordUsed() {
        issueTokenRequest(VALID_USER, "invalid").then().statusCode(400)
    }

    @Test
    @Throws(Exception::class)
    fun cannotGetTokenIfNotExistingUserUsed() {
        issueTokenRequest("invalid", "anything").then().statusCode(401)
    }

    @Test
    @Throws(Exception::class)
    fun canUseTokenToGetSecuredUserDetails() {
        val token = getToken(issueTokenRequest(VALID_USER, VALID_USER_PASS))
        given()
                .header(Header("Authorization", "Bearer " + token))
                .get("/me")
                .then()
                .statusCode(200)
    }

    @Test
    @Throws(Exception::class)
    fun canUseTemperedWithToken() {
        val token = getToken(issueTokenRequest(VALID_USER, VALID_USER_PASS))
        given()
                .header(Header("Authorization", "Bearer " + token + "x"))
                .get("/me")
                .then()
                .statusCode(401)
    }

    @Test
    @Throws(Exception::class)
    fun unauthorizedIfNoTokenPresent() {
        given()
                .get("/me")
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
        private val VALID_USER = "someone"
        private val VALID_USER_PASS = "valid"
        private val VALID_CLIENT_ID = "expenses-tracker-service"
        private val VALID_CLIENT_SECRET = "expenses-tracker-service-secret"
    }

}