package com.ingenious.spendwise.presentation.history

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Card
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.ingenious.spendwise.domain.model.Expense

@Composable
fun HistoryScreen(
    viewModel: HistoryViewModel = hiltViewModel(),
    onAddExpenseClick: () -> Unit,
    onEditExpense: (Expense) -> Unit
) {
    val state by viewModel.uiState.collectAsStateWithLifecycle()

    HistoryContent(
        state = state,
        onAddExpenseClick = onAddExpenseClick,
        onDeleteExpense = viewModel::deleteExpense,
        onEditExpense = onEditExpense
    )
}

@Composable
private fun HistoryContent(
    state: HistoryState,
    onAddExpenseClick: () -> Unit,
    onDeleteExpense: (Expense) -> Unit,
    onEditExpense: (Expense) -> Unit
) {
    Scaffold(
        floatingActionButton = {
            FloatingActionButton(
                onClick = onAddExpenseClick
            ) {
                Text("+")
            }
        }
    ) { paddingValues ->

        when {
            state.isLoading -> {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(paddingValues),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator()
                }
            }

            state.error != null -> {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(paddingValues),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = state.error,
                        color = MaterialTheme.colorScheme.error
                    )
                }
            }

            state.expenses.isEmpty() -> {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(paddingValues),
                    contentAlignment = Alignment.Center
                ) {
                    Text("No expenses yet")
                }
            }

            else -> {
                LazyColumn(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(paddingValues),
                    contentPadding = PaddingValues(16.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    items(
                        items = state.expenses,
                        key = { expense -> expense.id }
                    ) { expense ->

                        ExpenseItem(
                            expense = expense,
                            onDelete = {
                                onDeleteExpense(expense)
                            },
                            onEdit = {
                                onEditExpense(expense)
                            }
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun ExpenseItem(
    expense: Expense,
    onDelete: () -> Unit,
    onEdit: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onEdit)
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            Text(
                text = expense.title,
                style = MaterialTheme.typography.titleMedium
            )

            Text(
                text = "Rs. ${expense.amount}",
                style = MaterialTheme.typography.bodyLarge
            )

            Text(
                text = expense.category,
                style = MaterialTheme.typography.bodyMedium
            )

            expense.note?.let { note ->
                Text(
                    text = note,
                    style = MaterialTheme.typography.bodySmall
                )
            }
        }
        TextButton(
            onClick = onDelete
        ) {
            Text("Delete")
        }
    }
}
