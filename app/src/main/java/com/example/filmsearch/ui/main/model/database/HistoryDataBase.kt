package com.example.filmsearch.ui.main.model.database

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase

@Database(entities = [HistoryEntity::class], version = 2, exportSchema = true)  //передаем наши таблицы, передаем версию, экспортшема-показывает типа логов, кто куда ходил
abstract class HistoryDataBase : RoomDatabase() {

    object Migration1to2 : Migration(1, 2) {
        override fun migrate(db: SupportSQLiteDatabase) {
            db.execSQL("ALTER TABLE HistoryEntity ADD COLUMN notes TEXT DEFAULT 0 NOT NULL")
        }
    }
    //сам файл, в котором будет храниться таблица
    abstract fun historyDao(): HistoryDao //будет возвращать дао

}