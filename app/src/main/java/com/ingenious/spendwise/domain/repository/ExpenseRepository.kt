package com.ingenious.spendwise.domain.repository

import com.ingenious.spendwise.domain.model.Expense
import kotlinx.coroutines.flow.Flow

interface ExpenseRepository {

    suspend fun insertExpense(
        expense: Expense
    )

    suspend fun updateExpense(
        expense: Expense
    )

    suspend fun deleteExpense(
        expense: Expense
    )

    fun getAllExpenses(): Flow<List<Expense>>

    suspend fun getExpenseById(id: Long): Expense?
}