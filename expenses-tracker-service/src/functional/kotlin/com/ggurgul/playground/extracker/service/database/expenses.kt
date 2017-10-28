package com.ggurgul.playground.extracker.service.database

import com.ninja_squad.dbsetup.operation.Insert
import com.ninja_squad.dbsetup_kotlin.mappedValues
import java.math.BigDecimal
import java.util.*

enum class Expense(
        val expenseId: UUID,
        val date: Date,
        val amount: BigDecimal,
        val description: String,
        val comment: String
) {

    PS4(
            expenseId = UUID.fromString("00000000-0000-0000-0000-000000000001"),
            date = Date(),
            amount = BigDecimal.TEN,
            description = "PS4",
            comment = "Just a new item"
    )

}

fun insertExpense(insertBuilder: Insert.Builder, expense: Expense) {
    insertBuilder.mappedValues(
            "expense_id" to expense.expenseId,
            "date" to expense.date,
            "amount" to expense.amount,
            "description" to expense.description,
            "comment" to expense.comment
    )
}