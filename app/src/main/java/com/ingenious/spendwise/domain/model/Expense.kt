package com.ingenious.spendwise.domain.model

data class Expense(
    val id: Long,
    val title: String,
    val amount: Double,
    val category: String,
    val note: String?,
    val date: Long
)