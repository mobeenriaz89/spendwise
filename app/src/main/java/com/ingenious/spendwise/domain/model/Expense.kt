package com.ingenious.spendwise.domain.model

data class Expense(
    val id: Long,
    val title: String,
    val amountInPaisa: Long,
    val category: String,
    val note: String?,
    val date: Long
)