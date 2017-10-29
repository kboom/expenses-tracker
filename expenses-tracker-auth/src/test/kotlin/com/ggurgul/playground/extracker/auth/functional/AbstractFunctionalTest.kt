package com.ggurgul.playground.extracker.auth.functional

import io.restassured.RestAssured
import org.junit.Before
import org.junit.experimental.categories.Category
import org.junit.runner.RunWith
import org.springframework.boot.autoconfigure.jdbc.EmbeddedDatabaseConnection
import org.springframework.boot.context.embedded.LocalServerPort
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.ActiveProfiles
import org.springframework.test.context.junit4.SpringRunner

import org.springframework.boot.test.context.SpringBootTest.WebEnvironment.RANDOM_PORT

@Category(FunctionalTest::class)
@RunWith(SpringRunner::class)
@SpringBootTest(webEnvironment = RANDOM_PORT)
@ActiveProfiles("dev")
@AutoConfigureTestDatabase(connection = EmbeddedDatabaseConnection.H2)
abstract class AbstractFunctionalTest {

    @LocalServerPort
    private var port: Int? = null

    @Before
    fun before() {
        reset()
    }

    private fun reset() {
        RestAssured.reset()
        RestAssured.port = port!!
        RestAssured.enableLoggingOfRequestAndResponseIfValidationFails()
    }

}