package com.ingenious.spendwise.presentation.add_expense

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ingenious.spendwise.domain.model.Expense
import com.ingenious.spendwise.domain.usecase.GetExpenseByIdUseCase
import com.ingenious.spendwise.domain.usecase.UpdateExpenseUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import jakarta.inject.Inject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

@HiltViewModel
class EditExpenseViewModel @Inject constructor(
    private val getExpenseByIdUseCase: GetExpenseByIdUseCase,
    private val updateExpenseUseCase: UpdateExpenseUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow(EditExpenseState())

    val uiState: StateFlow<EditExpenseState> =
        _uiState.asStateFlow()

    private var expenseId: Long = 0
    private var originalDate: Long = 0
    fun loadExpense(id: Long) {
        expenseId = id

        viewModelScope.launch {
            try {
                val expense = getExpenseByIdUseCase(id)

                if (expense == null) {
                    _uiState.update {
                        it.copy(
                            isLoading = false,
                            error = "Expense not found"
                        )
                    }
                    return@launch
                }
                originalDate = expense.date
                _uiState.update {
                    it.copy(
                        title = expense.title,
                        amount = expense.amount.toString(),
                        category = expense.category,
                        note = expense.note.orEmpty(),
                        isLoading = false
                    )
                }

            } catch (e: Exception) {
                _uiState.update {
                    it.copy(
                        isLoading = false,
                        error = e.message
                    )
                }
            }
        }
    }

    fun onTitleChanged(value: String) {
        _uiState.update {
            it.copy(title = value)
        }
    }

    fun onAmountChanged(value: String) {
        _uiState.update {
            it.copy(amount = value)
        }
    }

    fun onCategoryChanged(value: String) {
        _uiState.update {
            it.copy(category = value)
        }
    }

    fun onNoteChanged(value: String) {
        _uiState.update {
            it.copy(note = value)
        }
    }

    fun updateExpense() {
        viewModelScope.launch {

            _uiState.update {
                it.copy(
                    isLoading = true,
                    error = null
                )
            }

            try {
                val state = _uiState.value

                val amount = state.amount.toDoubleOrNull()

                if (amount == null) {
                    _uiState.update {
                        it.copy(
                            isLoading = false,
                            error = "Please enter a valid amount"
                        )
                    }

                    return@launch
                }

                val expense = Expense(
                    id = expenseId,
                    title = state.title,
                    amount = amount,
                    category = state.category,
                    note = state.note.ifBlank { null },
                    date = originalDate
                )

                updateExpenseUseCase(expense)

                _uiState.update {
                    it.copy(
                        isLoading = false,
                        isUpdated = true
                    )
                }

            } catch (e: IllegalArgumentException) {
                _uiState.update {
                    it.copy(
                        isLoading = false,
                        error = e.message
                    )
                }
            } catch (e: Exception) {
                _uiState.update {
                    it.copy(
                        isLoading = false,
                        error = "Something went wrong"
                    )
                }
            }
        }
    }
}