package com.ingenious.spendwise.presentation.navigation

sealed class Screen(
    val route: String
) {
    data object AddExpense : Screen("add_expense")
}