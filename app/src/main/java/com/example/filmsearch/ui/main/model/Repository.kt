package com.example.filmsearch.ui.main.model

interface Repository {
    fun getFilmFromServer(id: Long): FilmDTO
    fun getPopularFilmFromServer(): FilmsList
}