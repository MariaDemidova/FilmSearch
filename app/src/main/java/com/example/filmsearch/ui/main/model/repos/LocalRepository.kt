package com.example.filmsearch.ui.main.model.repos

import androidx.room.Entity
import com.example.filmsearch.ui.main.model.Film
import com.example.filmsearch.ui.main.model.database.HistoryEntity

interface LocalRepository { //класс, который будет отвечать за работу с локальными данными
    fun getAllHistory(): List<HistoryEntity>
    fun saveEntity(film: HistoryEntity)
}