package com.ingenious.spendwise.presentation.dashboard

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.ingenious.spendwise.core.util.toPkrString
import com.ingenious.spendwise.domain.model.Expense
import java.util.Locale
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.ui.text.style.TextOverflow
import com.ingenious.spendwise.core.util.toExpenseDateString
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DashboardScreen(
    onAddExpense: () -> Unit,
    onExpenseClick: (Long) -> Unit,
    onViewAllExpenses: () -> Unit,
    viewModel: DashboardViewModel = hiltViewModel()
) {
    val state by viewModel.uiState.collectAsStateWithLifecycle()

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("SpendWise") }
            )
        }
    ) { innerPadding ->

        when {
            state.isLoading -> {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(innerPadding),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    CircularProgressIndicator()
                }
            }

            state.error != null -> {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(innerPadding)
                        .padding(16.dp),
                    verticalArrangement = Arrangement.Center
                ) {
                    Text(
                        text = state.error ?: "Something went wrong",
                        color = MaterialTheme.colorScheme.error
                    )
                }
            }

            else -> {
                val summary = state.summary

                LazyColumn(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(innerPadding)
                        .padding(horizontal = 16.dp),
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    item {
                        Spacer(Modifier.height(4.dp))

                        Card(
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Column(
                                modifier = Modifier.padding(20.dp)
                            ) {
                                Text(
                                    text = "Total spending this month",
                                    style = MaterialTheme.typography.titleMedium
                                )

                                Spacer(Modifier.height(8.dp))

                                Text(
                                    text = summary.monthlyTotalInPaisa.toPkrString(),
                                    style = MaterialTheme.typography.headlineMedium
                                )

                                Spacer(Modifier.height(8.dp))

                                Text(
                                    text = "${summary.monthlyExpenseCount} expenses this month",
                                    style = MaterialTheme.typography.bodyMedium
                                )
                            }
                        }
                    }

                    item {
                        Text(
                            text = "Spending by category",
                            style = MaterialTheme.typography.titleLarge
                        )

                        Spacer(Modifier.height(8.dp))

                        if (summary.categoryTotals.isEmpty()) {
                            Text("No expenses recorded this month yet.")
                        } else {
                            Column(
                                verticalArrangement = Arrangement.spacedBy(16.dp)
                            ) {
                                summary.categoryTotals
                                    .toList()
                                    .sortedByDescending { it.second }
                                    .forEach { (category, amount) ->

                                        val progress =
                                            if (summary.monthlyTotalInPaisa > 0L) {
                                                amount.toFloat() /
                                                        summary.monthlyTotalInPaisa.toFloat()
                                            } else {
                                                0f
                                            }

                                        Column(
                                            modifier = Modifier.fillMaxWidth(),
                                            verticalArrangement = Arrangement.spacedBy(6.dp)
                                        ) {
                                            Row(
                                                modifier = Modifier.fillMaxWidth(),
                                                horizontalArrangement =
                                                    Arrangement.SpaceBetween
                                            ) {
                                                Text(
                                                    text = category,
                                                    style = MaterialTheme.typography.bodyLarge
                                                )

                                                Text(
                                                    text = amount.toPkrString(),
                                                    style = MaterialTheme.typography.bodyMedium
                                                )
                                            }

                                            LinearProgressIndicator(
                                                progress = { progress.coerceIn(0f, 1f) },
                                                modifier = Modifier
                                                    .fillMaxWidth()
                                                    .height(8.dp)
                                            )

                                            Text(
                                                text = "${(progress * 100).toInt()}% of monthly spending",
                                                style = MaterialTheme.typography.bodySmall,
                                                color = MaterialTheme.colorScheme.onSurfaceVariant
                                            )
                                        }
                                    }
                            }
                        }
                    }

                    item {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = "Recent expenses",
                                style = MaterialTheme.typography.titleLarge
                            )

                            androidx.compose.material3.TextButton(
                                onClick = onViewAllExpenses
                            ) {
                                Text("View all")
                            }
                        }
                    }

                    if (summary.recentExpenses.isEmpty()) {
                        item {
                            Text("No expenses yet. Add your first expense!")
                        }
                    } else {
                        items(
                            items = summary.recentExpenses,
                            key = { expense -> expense.id }
                        ) { expense ->
                            RecentExpenseRow(
                                expense = expense,
                                onClick = {
                                    onExpenseClick(expense.id)
                                }
                            )
                        }
                    }

                    item {
                        Button(
                            onClick = onAddExpense,
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Text("Add expense")
                        }

                        Spacer(Modifier.height(16.dp))
                    }
                }
            }
        }
    }
}

@Composable
private fun RecentExpenseRow(
    expense: Expense,
    onClick: () -> Unit
) {
    androidx.compose.material3.TextButton(
        onClick = onClick,
        modifier = Modifier.fillMaxWidth()
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = expense.title,
                    color = MaterialTheme.colorScheme.onSurface
                )

                Text(
                    text = "${expense.category} • ${expense.date.toExpenseDateString()}",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
            }

            Text(
                text = expense.amountInPaisa.toPkrString(),
                color = MaterialTheme.colorScheme.onSurface
            )
        }
    }
}