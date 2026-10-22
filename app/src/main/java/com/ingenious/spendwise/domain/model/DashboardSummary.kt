package com.ingenious.spendwise.domain.model

data class DashboardSummary(
    val monthlyTotalInPaisa: Long = 0L,
    val monthlyExpenseCount: Int = 0,
    val categoryTotals: Map<String, Long> = emptyMap(),
    val recentExpenses: List<Expense> = emptyList()
)