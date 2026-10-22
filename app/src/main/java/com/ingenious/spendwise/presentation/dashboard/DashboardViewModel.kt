
package com.ingenious.spendwise.presentation.dashboard

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ingenious.spendwise.domain.usecase.GetDashboardSummaryUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn

@HiltViewModel
class DashboardViewModel @Inject constructor(
    getDashboardSummaryUseCase: GetDashboardSummaryUseCase
) : ViewModel() {

    val uiState = getDashboardSummaryUseCase()
        .map { summary ->
            DashboardState(
                summary = summary,
                isLoading = false
            )
        }
        .catch { throwable ->
            if (throwable is CancellationException) {
                throw throwable
            }

            emit(
                DashboardState(
                    isLoading = false,
                    error = throwable.message
                        ?: "Unable to load dashboard"
                )
            )
        }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000),
            initialValue = DashboardState()
        )
}