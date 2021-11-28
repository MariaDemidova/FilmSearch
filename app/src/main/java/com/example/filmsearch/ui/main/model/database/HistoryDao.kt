package com.example.filmsearch.ui.main.model.database

import androidx.room.*
import retrofit2.http.DELETE

@Dao
interface HistoryDao { //дата акцесс обжект =  управляет
    @Query("SELECT * FROM HistoryEntity") //запрос к базе данных (на языке sql
    fun all(): List<HistoryEntity> //метод будет возвращать нам все элементы в базе в виде листа

    @Query("SELECT * FROM HistoryEntity WHERE title LIKE :title") //поиск по слову со всеми вхождениями этого слова в поле title
    fun getDataByName(title: String): List<HistoryEntity>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun insert(entity: HistoryEntity)

    @Update
    fun update(entity: HistoryEntity)

    @Delete
    fun delete(entity: HistoryEntity)
//
//    @Query("DELETE FROM HistoryEntity")
//    fun clear(entity: HistoryEntity)
}