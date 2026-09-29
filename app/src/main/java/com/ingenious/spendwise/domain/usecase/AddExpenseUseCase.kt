package com.ingenious.spendwise.domain.usecase

import com.ingenious.spendwise.domain.model.Expense
import com.ingenious.spendwise.domain.repository.ExpenseRepository
import javax.inject.Inject

class AddExpenseUseCase @Inject constructor(
    private val repository: ExpenseRepository
) {

    suspend operator fun invoke(
        expense: Expense
    ) {

        require(expense.title.isNotBlank()) {
            "Title cannot be empty"
        }

        require(expense.amount > 0) {
            "Amount must be greater than zero"
        }

        require(expense.category.isNotBlank()) {
            "Category must be selected"
        }

        repository.insertExpense(expense)
    }
}