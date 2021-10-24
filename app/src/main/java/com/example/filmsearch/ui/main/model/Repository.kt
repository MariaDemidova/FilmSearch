package com.example.filmsearch.ui.main.model

interface Repository {
    fun getFilmFromServer(): Film
    fun getFilmFromLocalStorageRus(): List<Film>
    fun getFilmFromLocalStorageWorld(): List<Film>
}