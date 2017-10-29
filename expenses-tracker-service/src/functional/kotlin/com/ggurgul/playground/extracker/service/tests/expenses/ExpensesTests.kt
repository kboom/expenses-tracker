package com.ggurgul.playground.extracker.service.tests.expenses

import com.ggurgul.playground.extracker.service.tests.AbstractFunctionalTest
import com.github.tomakehurst.wiremock.core.WireMockConfiguration
import com.github.tomakehurst.wiremock.junit.WireMockRule
import io.restassured.RestAssured.given
import io.restassured.http.Header
import org.hamcrest.Matchers.equalTo
import org.hamcrest.Matchers.hasSize
import org.junit.Rule
import org.junit.Test
import org.springframework.http.HttpHeaders
import org.springframework.security.test.context.support.WithMockUser


internal class ExpensesTests : AbstractFunctionalTest() {

    @Test
    fun userSeesHisExpenses() {
        given()
                .header(Header(HttpHeaders.AUTHORIZATION, "Bearer xyz123"))
                .get("/expenses")
                .then()
                .statusCode(200)
                .body("_embedded.expenses", hasSize<String>(equalTo(1)))
                .body("_embedded.expenses[0].name", equalTo("Ps4"))
    }

}