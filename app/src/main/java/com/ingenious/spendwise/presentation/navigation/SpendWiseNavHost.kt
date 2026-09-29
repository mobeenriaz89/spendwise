package com.ingenious.spendwise.presentation.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.ingenious.spendwise.presentation.add_expense.AddExpenseScreen

@Composable
fun SpendWiseNavHost() {

    val navController = rememberNavController()

    NavHost(
        navController = navController,
        startDestination = Screen.AddExpense.route
    ) {

        composable(
            route = Screen.AddExpense.route
        ) {
            AddExpenseScreen(
                onExpenseSaved = {
                    // We will navigate to History later.
                }
            )
        }
    }
}