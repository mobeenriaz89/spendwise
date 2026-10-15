package com.ingenious.spendwise.data.local.database

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase
import com.ingenious.spendwise.data.local.dao.ExpenseDao
import com.ingenious.spendwise.data.local.entity.ExpenseEntity

@Database(
    entities = [ExpenseEntity::class],
    version = 2,
    exportSchema = false
)
abstract class ExpenseDatabase : RoomDatabase() {

    abstract fun expenseDao(): ExpenseDao

    companion object {
        val MIGRATION_1_2 = object : Migration(1, 2) {
            override fun migrate(db: SupportSQLiteDatabase) {

                // Create the new table with amount stored as integer paisa.
                db.execSQL(
                    """
                    CREATE TABLE IF NOT EXISTS expenses_new (
                        id INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL,
                        title TEXT NOT NULL,
                        amountInPaisa INTEGER NOT NULL,
                        category TEXT NOT NULL,
                        note TEXT,
                        date INTEGER NOT NULL
                    )
                    """.trimIndent()
                )

                // Copy all existing expenses and convert rupees to paisa.
                db.execSQL(
                    """
                    INSERT INTO expenses_new (
                        id, title, amountInPaisa, category, note, date
                    )
                    SELECT
                        id,
                        title,
                        CAST(ROUND(amount * 100.0) AS INTEGER),
                        category,
                        note,
                        date
                    FROM expenses
                    """.trimIndent()
                )

                // Replace the old table while preserving the expense records.
                db.execSQL("DROP TABLE expenses")
                db.execSQL(
                    "ALTER TABLE expenses_new RENAME TO expenses"
                )
            }
        }
    }
}