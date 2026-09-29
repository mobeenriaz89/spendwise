package com.ingenious.spendwise.data.repository

import com.ingenious.spendwise.data.local.dao.ExpenseDao
import com.ingenious.spendwise.data.mapper.toDomain
import com.ingenious.spendwise.data.mapper.toEntity
import com.ingenious.spendwise.domain.model.Expense
import com.ingenious.spendwise.domain.repository.ExpenseRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class ExpenseRepositoryImpl @Inject constructor(
    private val expenseDao: ExpenseDao
) : ExpenseRepository {

    override suspend fun insertExpense(
        expense: Expense
    ) {
        expenseDao.insertExpense(
            expense.toEntity()
        )
    }

    override suspend fun updateExpense(
        expense: Expense
    ) {
        expenseDao.updateExpense(
            expense.toEntity()
        )
    }

    override suspend fun deleteExpense(
        expense: Expense
    ) {
        expenseDao.deleteExpense(
            expense.toEntity()
        )
    }

    override fun getAllExpenses(): Flow<List<Expense>> {
        return expenseDao
            .getAllExpenses()
            .map { entities ->
                entities.map { it.toDomain() }
            }
    }
}