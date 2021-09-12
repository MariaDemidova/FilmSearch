package com.example.filmsearch.ui.main.model

import com.example.filmsearch.R

class RepositoryImpl : Repository {

    override fun getFilmFromServer(): Film = Film()
    
    override fun getFilmFromLocalStorageRus(): List<Film> = getRussianFilm()

    override fun getFilmFromLocalStorageWorld(): List<Film> = getWorldFilm()

}