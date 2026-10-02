package com.ingenious.spendwise.presentation.history

import com.ingenious.spendwise.domain.model.Expense

data class HistoryState(
    val expenses: List<Expense> = emptyList(),
    val isLoading: Boolean = true,
    val error: String? = null
)