package com.example.filmsearch.ui.main.model

import com.example.filmsearch.ui.main.model.Film

interface Repository {
    fun getFilmFromServer(): Film
    fun getFilmFromLocalStorage(): List<Film>

}