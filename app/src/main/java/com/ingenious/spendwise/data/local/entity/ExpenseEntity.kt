package com.ingenious.spendwise.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "expenses")
data class ExpenseEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,

    val title: String,

    val amountInPaisa: Long,

    val category: String,

    val note: String? = null,

    val date: Long
)