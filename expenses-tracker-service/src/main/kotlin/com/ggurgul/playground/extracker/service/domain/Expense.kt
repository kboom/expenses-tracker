package com.ggurgul.playground.extracker.service.domain

import com.ggurgul.playground.extracker.service.domain.ExpenseId.Companion.noExpenseId
import java.math.BigDecimal
import java.util.*
import javax.persistence.Column
import javax.persistence.EmbeddedId
import javax.persistence.Entity
import javax.persistence.Table

//date, time, description, amount, comment.
@Entity
@Table(name = "expense")
class Expense(

        @EmbeddedId
        val expenseId: ExpenseId = noExpenseId,

        @Column(name = "date")
        val date: Date,

        @Column(name = "amount")
        val amount: BigDecimal,

        @Column(name = "description")
        val description: String,

        @Column(name = "comment")
        val comment: String

)