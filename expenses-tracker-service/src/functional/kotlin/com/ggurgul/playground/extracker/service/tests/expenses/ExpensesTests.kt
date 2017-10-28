package com.ggurgul.playground.extracker.service.tests.expenses

import com.ggurgul.playground.extracker.service.database.Expense
import com.ggurgul.playground.extracker.service.database.insertExpense
import com.ggurgul.playground.extracker.service.rules.DataLoadingRule
import com.ggurgul.playground.extracker.service.rules.ReadOnly
import com.ggurgul.playground.extracker.service.tests.AbstractFunctionalTest
import io.restassured.RestAssured.given
import org.hamcrest.Matchers.equalTo
import org.hamcrest.Matchers.hasSize
import org.junit.Rule
import org.junit.Test
import org.junit.rules.RuleChain
import org.junit.rules.TestRule

internal class ExpensesTests : AbstractFunctionalTest() {

    private val dataLoadingRule = DataLoadingRule({
        insertInto("expense") {
            insertExpense(this, Expense.PS4)
        }
    })

    @get:Rule
    var chain: TestRule = RuleChain.outerRule(dataLoadingRule)

    @Test
    @ReadOnly
    fun userSeesHisExpenses() {
        given()
//                .header("Authorization", authenticationRule.token)
                .get("/expenses")
                .then()
                .statusCode(200)
                .body("_embedded.expenses", hasSize<String>(equalTo(1)))
                .body("_embedded.expenses[0].name", equalTo("Ps4"))
    }

}