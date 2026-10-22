package com.ingenious.spendwise.domain.usecase

import com.ingenious.spendwise.domain.model.DashboardSummary
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import java.time.YearMonth
import java.time.ZoneId
import javax.inject.Inject

class GetDashboardSummaryUseCase @Inject constructor(
    private val getExpensesUseCase: GetExpensesUseCase
) {

    operator fun invoke(): Flow<DashboardSummary> {
        return getExpensesUseCase().map { expenses ->

            val zone = ZoneId.systemDefault()
            val currentMonth = YearMonth.now()

            val monthStart = currentMonth
                .atDay(1)
                .atStartOfDay(zone)
                .toInstant()
                .toEpochMilli()

            val nextMonthStart = currentMonth
                .plusMonths(1)
                .atDay(1)
                .atStartOfDay(zone)
                .toInstant()
                .toEpochMilli()

            val monthlyExpenses = expenses.filter { expense ->
                expense.date >= monthStart &&
                        expense.date < nextMonthStart
            }

            val monthlyTotal = monthlyExpenses.sumOf {
                it.amountInPaisa
            }

            val categoryTotals = monthlyExpenses
                .groupBy { it.category }
                .mapValues { (_, categoryExpenses) ->
                    categoryExpenses.sumOf {
                        it.amountInPaisa
                    }
                }

            val recentExpenses = expenses
                .sortedByDescending { it.date }
                .take(5)

            DashboardSummary(
                monthlyTotalInPaisa = monthlyTotal,
                monthlyExpenseCount = monthlyExpenses.size,
                categoryTotals = categoryTotals,
                recentExpenses = recentExpenses
            )
        }
    }
}