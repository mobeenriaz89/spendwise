package com.ingenious.spendwise.core.util

import java.time.Instant
import java.time.ZoneId
import java.time.format.DateTimeFormatter

private val expenseDateFormatter =
    DateTimeFormatter.ofPattern("dd MMM yyyy")

fun Long.toExpenseDateString(): String {
    return Instant.ofEpochMilli(this)
        .atZone(ZoneId.systemDefault())
        .toLocalDate()
        .format(expenseDateFormatter)
}