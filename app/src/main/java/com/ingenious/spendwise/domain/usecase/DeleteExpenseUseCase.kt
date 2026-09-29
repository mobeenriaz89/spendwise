package com.ingenious.spendwise.domain.usecase

import com.ingenious.spendwise.domain.model.Expense
import com.ingenious.spendwise.domain.repository.ExpenseRepository
import javax.inject.Inject

class DeleteExpenseUseCase @Inject constructor(
    private val repository: ExpenseRepository
) {
    suspend operator fun invoke(expense: Expense) {
        repository.deleteExpense(expense)
    }
}
