package com.ingenious.spendwise.presentation.add_expense

data class AddExpenseState(
    val title: String = "",
    val amount: String = "",
    val category: String = "",
    val note: String = "",
    val isLoading: Boolean = false,
    val error: String? = null,
    val isSaved: Boolean = false
)
