package com.ggurgul.playground.extracker.auth.functional

import io.restassured.RestAssured
import org.junit.Before
import org.junit.experimental.categories.Category
import org.junit.runner.RunWith
import org.springframework.boot.autoconfigure.jdbc.EmbeddedDatabaseConnection
import org.springframework.boot.context.embedded.LocalServerPort
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment.RANDOM_PORT
import org.springframework.boot.test.mock.mockito.MockBean
import org.springframework.mail.MailSender
import org.springframework.test.context.ActiveProfiles
import org.springframework.test.context.junit4.SpringRunner

@Category(FunctionalTest::class)
@RunWith(SpringRunner::class)
@SpringBootTest(webEnvironment = RANDOM_PORT)
@ActiveProfiles("dev")
@AutoConfigureTestDatabase(connection = EmbeddedDatabaseConnection.H2)
abstract class AbstractFunctionalTest {

    @LocalServerPort
    private var appPort: Int? = null

    @MockBean
    private lateinit var mailSender: MailSender

    @Before
    fun before() {
        reset()
    }

    private fun reset() {
        RestAssured.reset()
        RestAssured.port = appPort!!
        RestAssured.enableLoggingOfRequestAndResponseIfValidationFails()
    }

}