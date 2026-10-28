package com.ingenious.spendwise.domain.usecase

import com.ingenious.spendwise.domain.model.Expense
import com.ingenious.spendwise.domain.repository.ExpenseRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf

class FakeExpenseRepository(
    private val expenses: List<Expense>
) : ExpenseRepository {

    override suspend fun insertExpense(expense: Expense) {
        TODO("Not needed for dashboard tests")
    }

    override suspend fun updateExpense(expense: Expense) {
        TODO("Not needed for dashboard tests")
    }

    override suspend fun deleteExpense(expense: Expense) {
        TODO("Not needed for dashboard tests")
    }

    override fun getAllExpenses(): Flow<List<Expense>> =
        flowOf(expenses)

    override suspend fun getExpenseById(id: Long): Expense? =
        expenses.find { it.id == id }
}
