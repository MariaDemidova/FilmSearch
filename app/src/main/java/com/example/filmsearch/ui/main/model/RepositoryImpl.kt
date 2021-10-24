package com.example.filmsearch.ui.main.model

class RepositoryImpl : Repository {

    override fun getFilmFromServer(): Film = Film()

    override fun getFilmFromLocalStorageRus(): List<Film> = getRussianFilm()

    override fun getFilmFromLocalStorageWorld(): List<Film> = getWorldFilm()

}