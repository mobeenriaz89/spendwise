package com.ingenious.spendwise.presentation.dashboard

import com.ingenious.spendwise.domain.model.DashboardSummary

data class DashboardState(
    val summary: DashboardSummary = DashboardSummary(),
    val isLoading: Boolean = true,
    val error: String? = null
)
