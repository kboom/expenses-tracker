package com.ggurgul.playground.extracker.service.domain.repository

import com.ggurgul.playground.extracker.service.domain.Expense
import com.ggurgul.playground.extracker.service.domain.ExpenseId
import org.springframework.data.repository.PagingAndSortingRepository
import org.springframework.data.rest.core.annotation.RepositoryRestResource

@RepositoryRestResource
interface ExpensesRepository : PagingAndSortingRepository<Expense, ExpenseId>