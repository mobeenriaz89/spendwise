package com.ingenious.spendwise.presentation.add_expense

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
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

    EditExpenseContent(
        state = state,
        onTitleChanged = viewModel::onTitleChanged,
        onAmountChanged = viewModel::onAmountChanged,
        onCategoryChanged = viewModel::onCategoryChanged,
        onNoteChanged = viewModel::onNoteChanged,
        onUpdateClick = viewModel::updateExpense
    )
}

@Composable
private fun EditExpenseContent(
    state: EditExpenseState,
    onTitleChanged: (String) -> Unit,
    onAmountChanged: (String) -> Unit,
    onCategoryChanged: (String) -> Unit,
    onNoteChanged: (String) -> Unit,
    onUpdateClick: () -> Unit
) {
    if (state.isLoading) {
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            CircularProgressIndicator()
        }

        return
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {

        Text(
            text = "Edit Expense",
            style = MaterialTheme.typography.headlineMedium
        )

        OutlinedTextField(
            value = state.title,
            onValueChange = onTitleChanged,
            modifier = Modifier.fillMaxWidth(),
            label = {
                Text("Title")
            },
            singleLine = true
        )

        OutlinedTextField(
            value = state.amount,
            onValueChange = onAmountChanged,
            modifier = Modifier.fillMaxWidth(),
            label = {
                Text("Amount")
            },
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Decimal
            ),
            singleLine = true
        )

        OutlinedTextField(
            value = state.category,
            onValueChange = onCategoryChanged,
            modifier = Modifier.fillMaxWidth(),
            label = {
                Text("Category")
            },
            singleLine = true
        )

        OutlinedTextField(
            value = state.note,
            onValueChange = onNoteChanged,
            modifier = Modifier.fillMaxWidth(),
            label = {
                Text("Note")
            }
        )

        state.error?.let {
            Text(
                text = it,
                color = MaterialTheme.colorScheme.error
            )
        }

        Button(
            onClick = onUpdateClick,
            modifier = Modifier.fillMaxWidth(),
            enabled = !state.isLoading
        ) {
            Text("Update Expense")
        }
    }
}