package com.example.filmsearch.ui.main.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.filmsearch.ui.main.model.database.HistoryEntity
import com.example.filmsearch.ui.main.model.repos.LocalRepository
import com.example.filmsearch.ui.main.model.repos.LocalRepositoryImpl
import com.example.filmsearch.ui.main.view.App

class HistoryViewModel : ViewModel() {

    private val historyRepository = LocalRepositoryImpl(App.getHistoryDao())

    fun getAllHistory(): List<HistoryEntity> = historyRepository.getAllHistory()

}