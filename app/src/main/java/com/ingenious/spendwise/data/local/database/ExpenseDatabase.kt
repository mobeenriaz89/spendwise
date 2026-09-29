package com.ingenious.spendwise.data.local.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.ingenious.spendwise.data.local.dao.ExpenseDao
import com.ingenious.spendwise.data.local.entity.ExpenseEntity


@Database(
    entities = [ExpenseEntity::class],
    version = 1,
    exportSchema = false
)
abstract class ExpenseDatabase: RoomDatabase() {
    abstract val expenseDao: ExpenseDao
}