package com.ingenious.spendwise.presentation.add_expense

data class EditExpenseState(
    val title: String = "",
    val amount: String = "",
    val category: String = "",
    val note: String = "",
    val isLoading: Boolean = true,
    val error: String? = null,
    val isUpdated: Boolean = false
)