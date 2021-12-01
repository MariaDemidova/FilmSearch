package com.example.filmsearch.ui.main.model.repos

import com.example.filmsearch.ui.main.model.Film
import com.example.filmsearch.ui.main.model.FilmModel


interface Repository  {
    fun getFilmFromServer(): Film

     fun getPopularFilmsByRetro(callback: retrofit2.Callback<FilmModel>)

}