package com.example.filmsearch.ui.main.model.repos

import androidx.room.Entity
import com.example.filmsearch.ui.main.model.Film
import com.example.filmsearch.ui.main.model.database.HistoryDao
import com.example.filmsearch.ui.main.model.database.HistoryEntity

class LocalRepositoryImpl(private val dao: HistoryDao) : LocalRepository {

    override fun getAllHistory(): List<HistoryEntity> = dao.all()

    override fun saveEntity(film: HistoryEntity) {
        dao.insert(film)
    }
}