package com.ingenious.spendwise.presentation.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.ingenious.spendwise.presentation.add_expense.AddExpenseScreen
import com.ingenious.spendwise.presentation.history.HistoryScreen

@Composable
fun SpendWiseNavHost() {

    val navController = rememberNavController()

    NavHost(
        navController = navController,
        startDestination = Screen.History.route
    ) {
        composable(Screen.History.route) {
            HistoryScreen(
                onAddExpenseClick = {
                    navController.navigate(
                        Screen.AddExpense.route
                    )
                },
                onEditExpense = { expense ->
                    navController.navigate(
                        Screen.EditExpense.createRoute(expense.id)
                    )
                }
            )
        }


        composable(
            route = Screen.AddExpense.route
        ) {
            AddExpenseScreen(
                onExpenseSaved = {
                    navController.popBackStack()
                }
            )
        }

        composable(
            route = Screen.EditExpense.route
        ) { backStackEntry ->

            val expenseId = backStackEntry
                .arguments
                ?.getLong("expenseId")
                ?: return@composable

            EditExpenseScreen(
                expenseId = expenseId,
                onExpenseUpdated = {
                    navController.popBackStack()
                }
            )
        }
    }
}