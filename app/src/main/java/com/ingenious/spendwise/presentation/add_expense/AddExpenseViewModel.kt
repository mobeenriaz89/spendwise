package com.ingenious.spendwise.presentation.add_expense

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ingenious.spendwise.core.util.toPaisaOrNull
import com.ingenious.spendwise.domain.model.Expense
import com.ingenious.spendwise.domain.usecase.AddExpenseUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AddExpenseViewModel @Inject constructor(
    private val addExpenseUseCase: AddExpenseUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow(AddExpenseState())
    val uiState: StateFlow<AddExpenseState> = _uiState.asStateFlow()
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

    fun saveExpense() {
        viewModelScope.launch {
            _uiState.update {
                it.copy(
                    isLoading = true,
                    error = null
                )
            }

            try {
                val state = _uiState.value

                val amountInPaisa = state.amount.toPaisaOrNull()

                if (amountInPaisa == null) {
                    _uiState.update {
                        it.copy(
                            isLoading = false,
                            error = "Enter a valid amount with up to 2 decimal places"
                        )
                    }
                    return@launch
                }

                val expense = Expense(
                    id = 0,
                    title = state.title,
                    amountInPaisa = amountInPaisa,
                    category = state.category,
                    note = state.note.ifBlank { null },
                    date = System.currentTimeMillis()
                )

                addExpenseUseCase(expense)

                _uiState.update {
                    it.copy(
                        isLoading = false,
                        isSaved = true
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
