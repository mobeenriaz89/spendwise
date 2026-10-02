package com.ingenious.spendwise.domain.usecase

import com.ingenious.spendwise.domain.model.Expense
import com.ingenious.spendwise.domain.repository.ExpenseRepository
import jakarta.inject.Inject

class GetExpenseByIdUseCase @Inject constructor(
    private val repository: ExpenseRepository
) {

    suspend operator fun invoke(id: Long): Expense? {
        return repository.getExpenseById(id)
    }
}