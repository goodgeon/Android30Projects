package example.calculator

import androidx.room.Database
import androidx.room.RoomDatabase
import example.calculator.dao.HistoryDao
import example.calculator.model.History

@Database(entities = [History::class], version = 1)
abstract class AppDatabase: RoomDatabase() {
    abstract fun historyDao(): HistoryDao
}