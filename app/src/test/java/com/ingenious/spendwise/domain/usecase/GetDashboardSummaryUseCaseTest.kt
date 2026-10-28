package com.ingenious.spendwise.domain.usecase

import com.ingenious.spendwise.domain.model.Expense
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Test
import java.time.LocalDate
import java.time.YearMonth
import java.time.ZoneId

class GetDashboardSummaryUseCaseTest {

    private val zone = ZoneId.systemDefault()

    private fun dateInCurrentMonth(day: Int): Long {
        val date = YearMonth.now().atDay(day.coerceAtMost(
            YearMonth.now().lengthOfMonth()
        ))

        return date.atTime(12, 0)
            .atZone(zone)
            .toInstant()
            .toEpochMilli()
    }

    private fun dateInPreviousMonth(): Long {
        return YearMonth.now()
            .minusMonths(1)
            .atDay(15)
            .atTime(12, 0)
            .atZone(zone)
            .toInstant()
            .toEpochMilli()
    }

    private fun createExpense(
        id: Long,
        amountInPaisa: Long,
        category: String,
        date: Long
    ) = Expense(
        id = id,
        title = "Expense $id",
        amountInPaisa = amountInPaisa,
        category = category,
        note = null,
        date = date
    )

    @Test
    fun `monthly totals exclude expenses from previous month`() = runTest {
        val expenses = listOf(
            createExpense(1, 10_000, "Food", dateInCurrentMonth(5)),
            createExpense(2, 5_000, "Transport", dateInCurrentMonth(10)),
            createExpense(3, 20_000, "Food", dateInPreviousMonth())
        )

        val repository = FakeExpenseRepository(expenses)
        val getExpenses = GetExpensesUseCase(repository)
        val useCase = GetDashboardSummaryUseCase(getExpenses)

        val result = useCase().first()

        assertEquals(15_000L, result.monthlyTotalInPaisa)
        assertEquals(2, result.monthlyExpenseCount)
    }

    @Test
    fun `category totals include only current month expenses`() = runTest {
        val expenses = listOf(
            createExpense(1, 10_000, "Food", dateInCurrentMonth(5)),
            createExpense(2, 5_000, "Food", dateInCurrentMonth(10)),
            createExpense(3, 7_000, "Transport", dateInCurrentMonth(12)),
            createExpense(4, 20_000, "Food", dateInPreviousMonth())
        )

        val repository = FakeExpenseRepository(expenses)
        val useCase = GetDashboardSummaryUseCase(
            GetExpensesUseCase(repository)
        )

        val result = useCase().first()

        assertEquals(15_000L, result.categoryTotals["Food"])
        assertEquals(7_000L, result.categoryTotals["Transport"])
    }

    @Test
    fun `recent expenses are newest first and limited to five`() = runTest {
        val expenses = (1L..7L).map { id ->
            createExpense(
                id = id,
                amountInPaisa = id * 100,
                category = "Food",
                date = dateInCurrentMonth(id.toInt())
            )
        }

        val repository = FakeExpenseRepository(expenses)
        val useCase = GetDashboardSummaryUseCase(
            GetExpensesUseCase(repository)
        )

        val result = useCase().first()

        assertEquals(5, result.recentExpenses.size)
        assertEquals(
            listOf(7L, 6L, 5L, 4L, 3L),
            result.recentExpenses.map { it.id }
        )
    }

    @Test
    fun `empty expenses produce an empty dashboard summary`() = runTest {
        val repository = FakeExpenseRepository(emptyList())
        val useCase = GetDashboardSummaryUseCase(
            GetExpensesUseCase(repository)
        )

        val result = useCase().first()

        assertEquals(0L, result.monthlyTotalInPaisa)
        assertEquals(0, result.monthlyExpenseCount)
        assertEquals(emptyMap<String, Long>(), result.categoryTotals)
        assertEquals(emptyList<Expense>(), result.recentExpenses)
    }
}
