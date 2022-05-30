package com.example.a12bookreview.model

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.a12bookreview.dao.HistoryDao

@Database(entities = [History::class], version = 1, exportSchema = false)
abstract class AppDatabase : RoomDatabase() {
    abstract fun historyDao(): HistoryDao

}