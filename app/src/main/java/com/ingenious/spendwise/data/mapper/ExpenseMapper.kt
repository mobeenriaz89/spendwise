package com.ingenious.spendwise.data.mapper

import com.ingenious.spendwise.data.local.entity.ExpenseEntity
import com.ingenious.spendwise.domain.model.Expense

    fun ExpenseEntity.toDomain(): Expense {
        return Expense(
            id = id,
            title = title,
            amountInPaisa = amountInPaisa,
            category = category,
            note = note,
            date = date
        )
    }

    fun Expense.toEntity(): ExpenseEntity {
        return ExpenseEntity(
            id = id,
            title = title,
            amountInPaisa = amountInPaisa,
            category = category,
            note = note,
            date = date
        )
    }