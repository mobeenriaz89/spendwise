package com.ingenious.spendwise.presentation.add_expense

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle

@Composable
fun AddExpenseScreen(
    viewModel: AddExpenseViewModel = hiltViewModel(),
    onExpenseSaved: () -> Unit
) {
    val state by viewModel.uiState.collectAsStateWithLifecycle()

    LaunchedEffect(state.isSaved) {
        if (state.isSaved) {
            onExpenseSaved()
        }
    }

    Column {
        Text(
            text = "Add Expense",
            style = MaterialTheme.typography.headlineMedium,
            modifier = Modifier.padding(16.dp)
        )

        ExpenseForm(
            modifier = Modifier.weight(1f),
            title = state.title,
            amount = state.amount,
            category = state.category,
            note = state.note,
            error = state.error,
            isLoading = state.isLoading,
            buttonText = "Save Expense",
            onTitleChanged = viewModel::onTitleChanged,
            onAmountChanged = viewModel::onAmountChanged,
            onCategoryChanged = viewModel::onCategoryChanged,
            onNoteChanged = viewModel::onNoteChanged,
            onSubmit = viewModel::saveExpense
        )
    }
}


