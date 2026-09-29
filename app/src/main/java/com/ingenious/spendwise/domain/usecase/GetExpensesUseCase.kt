package com.ingenious.spendwise.domain.usecase

import com.ingenious.spendwise.domain.model.Expense
import com.ingenious.spendwise.domain.repository.ExpenseRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetExpensesUseCase @Inject constructor(
    private val repository: ExpenseRepository
) {
    operator fun invoke(): Flow<List<Expense>> {return repository.getAllExpenses() }
}
