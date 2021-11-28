package com.example.filmsearch.ui.main.model.database

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class HistoryEntity(
    @PrimaryKey(autoGenerate = true) //ключ, который будет главным
    val id: Long,
    val title: String,
    val timestamp: Long,
    val notes: String
) { //класс, который описывает таблицу


}