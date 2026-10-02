package com.ingenious.spendwise.presentation.history

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ingenious.spendwise.domain.model.Expense
import com.ingenious.spendwise.domain.usecase.DeleteExpenseUseCase
import com.ingenious.spendwise.domain.usecase.GetExpensesUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HistoryViewModel @Inject constructor(
    getExpensesUseCase: GetExpensesUseCase,
    private val deleteExpenseUseCase: DeleteExpenseUseCase
) : ViewModel() {

    val uiState: StateFlow<HistoryState> =
        getExpensesUseCase()
            .map { expenses ->
                HistoryState(
                    expenses = expenses,
                    isLoading = false
                )
            }
            .catch { exception ->
                emit(
                    HistoryState(
                        isLoading = false,
                        error = exception.message
                    )
                )
            }
            .stateIn(
                scope = viewModelScope,
                started = SharingStarted.WhileSubscribed(5_000),
                initialValue = HistoryState()
            )

    fun deleteExpense(expense: Expense) {
        viewModelScope.launch {
            try {
                deleteExpenseUseCase(expense)
            } catch (e: Exception) {
                // We'll improve error handling later.
            }
        }
    }
}
