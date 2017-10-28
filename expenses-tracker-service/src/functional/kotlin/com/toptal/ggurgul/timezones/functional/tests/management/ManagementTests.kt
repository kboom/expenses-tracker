package com.toptal.ggurgul.timezones.functional.tests.management

import com.toptal.ggurgul.timezones.functional.tests.AbstractFunctionalTest
import io.restassured.RestAssured.get
import org.hamcrest.Matchers.equalTo
import org.junit.Test

internal class ManagementTests : AbstractFunctionalTest() {

    @Test
    fun canGetHeartbeat() {
        get("/health/heartbeat")
                .then()
                .statusCode(200)
                .body(equalTo("ping"))
    }

}