package com.ingenious.spendwise.core.util

import java.math.BigDecimal
import java.text.NumberFormat
import java.util.Locale

fun Long.toPkrString(): String {
    val rupees = BigDecimal.valueOf(this, 2)

    return "Rs. " + NumberFormat.getNumberInstance(
        Locale("en", "PK")
    ).apply {
        minimumFractionDigits = 2
        maximumFractionDigits = 2
    }.format(rupees)
}

fun String.toPaisaOrNull(): Long? {
    return try {
        BigDecimal(trim())
            .movePointRight(2)
            .longValueExact()
    } catch (e: NumberFormatException) {
        null
    } catch (e: ArithmeticException) {
        null
    }
}