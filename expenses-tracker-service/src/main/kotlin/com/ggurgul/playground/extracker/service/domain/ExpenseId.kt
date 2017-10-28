package com.ggurgul.playground.extracker.service.domain

import java.io.Serializable
import java.util.*
import javax.persistence.Column
import javax.persistence.Embeddable

@Embeddable
class ExpenseId(

    @Column(name = "expense_id", nullable = false, updatable = false)
    val id: UUID

): Serializable {

    companion object {

        val noExpenseId = ExpenseId(UUID.fromString("00000000-0000-0000-0000-000000000000"))

        fun expenseId(id: String): ExpenseId {
            return ExpenseId(UUID.fromString(id))
        }

        fun newExpenseId(): ExpenseId {
            return expenseId(UUID.randomUUID().toString())
        }

    }

}