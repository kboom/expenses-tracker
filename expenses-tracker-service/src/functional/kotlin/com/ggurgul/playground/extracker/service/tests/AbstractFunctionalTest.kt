package com.ggurgul.playground.extracker.service.tests

import com.ggurgul.playground.extracker.service.FunctionalTest
import io.restassured.RestAssured
import io.restassured.builder.RequestSpecBuilder
import org.junit.Before
import org.junit.experimental.categories.Category
import org.junit.runner.RunWith
import org.springframework.boot.autoconfigure.jdbc.EmbeddedDatabaseConnection
import org.springframework.boot.context.embedded.LocalServerPort
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment.RANDOM_PORT
import org.springframework.test.context.ActiveProfiles
import org.springframework.test.context.junit4.SpringRunner

@Category(FunctionalTest::class)
@RunWith(SpringRunner::class)
@SpringBootTest(webEnvironment = RANDOM_PORT)
@ActiveProfiles("dev")
@AutoConfigureTestDatabase(connection = EmbeddedDatabaseConnection.H2)
abstract class AbstractFunctionalTest {

    @LocalServerPort
    protected var port: Int? = null

    @Before
    fun before() {
        RestAssured.port = port!!
        RestAssured.basePath = "/api"
        RestAssured.requestSpecification = RequestSpecBuilder()
                .setContentType("application/json")
                .setAccept("application/json")
                .build()
        RestAssured.enableLoggingOfRequestAndResponseIfValidationFails()
    }

}