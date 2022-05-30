package com.example.a12bookreview.model

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase
import com.example.a12bookreview.dao.HistoryDao
import com.example.a12bookreview.dao.ReviewDao

@Database(entities = [History::class, Review::class], version = 2, exportSchema = false)
abstract class AppDatabase : RoomDatabase() {
    abstract fun historyDao(): HistoryDao
    abstract fun reviewDao(): ReviewDao
}

fun getAppDatabase(context: Context): AppDatabase {
    val migration_1_2 = object: Migration(1,2) {
        override fun migrate(database: SupportSQLiteDatabase) {
            database.execSQL("CREATE TABLE `REVIEW` (`id` INTEGER NOT NULL, `review` TEXT NOT NULL," + "PRIMARY KEY(`id`))")
        }

    }
    return Room.databaseBuilder(context, AppDatabase::class.java, "BookSearchDB").addMigrations(migration_1_2).build()
}