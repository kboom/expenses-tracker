package com.toptal.ggurgul.timezones.functional.tests

import com.toptal.ggurgul.timezones.functional.FunctionalTest
import io.restassured.RestAssured
import io.restassured.builder.RequestSpecBuilder
import org.junit.BeforeClass
import org.junit.experimental.categories.Category


@Category(FunctionalTest::class)
abstract class AbstractFunctionalTest {

    companion object {

        @BeforeClass
        @JvmStatic
        fun beforeClass() {
            RestAssured.port = 8080;
            RestAssured.basePath = "/api"
            RestAssured.requestSpecification = RequestSpecBuilder()
                    .setContentType("application/json")
                    .setAccept("application/json")
                    .build()
            RestAssured.enableLoggingOfRequestAndResponseIfValidationFails()
        }

    }

}