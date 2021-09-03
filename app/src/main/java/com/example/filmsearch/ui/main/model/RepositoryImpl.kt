package com.example.filmsearch.ui.main.model

import com.example.filmsearch.R

class RepositoryImpl : Repository {

    override fun getFilmFromServer(): Film = Film()

    override fun getFilmFromLocalStorage(): List<Film> {
        return listOf(
           Film(imageIndex = R.drawable.pi2hi),
           Film("Старая кошатница", "musical", 1950, R.drawable.kosh),
           Film("Крокодилл против саранчи", "horror", 2021,  R.drawable.croc),
           Film("Доброе утро в тюрьме", "documentary", 1991,  R.drawable.utro),
        )
    }

}