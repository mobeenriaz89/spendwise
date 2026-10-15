package com.ingenious.spendwise.presentation.add_expense

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle

@Composable
fun EditExpenseScreen(
    expenseId: Long,
    viewModel: EditExpenseViewModel = hiltViewModel(),
    onExpenseUpdated: () -> Unit
) {
    val state by viewModel.uiState.collectAsStateWithLifecycle()

    LaunchedEffect(expenseId) {
        viewModel.loadExpense(expenseId)
    }

    LaunchedEffect(state.isUpdated) {
        if (state.isUpdated) {
            onExpenseUpdated()
        }
    }
    if (state.isLoading) {
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            CircularProgressIndicator()
        }

        return
    }
    Column {
        Text(
            text = "Edit Expense",
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
            buttonText = "Update Expense",
            onTitleChanged = viewModel::onTitleChanged,
            onAmountChanged = viewModel::onAmountChanged,
            onCategoryChanged = viewModel::onCategoryChanged,
            onNoteChanged = viewModel::onNoteChanged,
            onSubmit = viewModel::updateExpense
        )
    }
}