package com.example.filmsearch.ui.main.viewmodel

import com.example.filmsearch.ui.main.model.Film

sealed class AppState {
    data class Success(val filmsList: List<Film>): AppState()
    data class Error(val error: Throwable) : AppState()
    object Loading : AppState()
}