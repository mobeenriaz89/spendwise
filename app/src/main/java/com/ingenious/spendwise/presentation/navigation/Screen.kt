package com.ingenious.spendwise.presentation.navigation

sealed class Screen(
    val route: String
) {
    data object AddExpense : Screen("add_expense")
    data object History : Screen("history")
    data object EditExpense : Screen("edit_expense/{expenseId}") {

        fun createRoute(expenseId: Long): String {
            return "edit_expense/$expenseId"
        }
    }
}