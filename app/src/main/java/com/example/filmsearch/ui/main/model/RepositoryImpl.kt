package com.example.filmsearch.ui.main.model

import com.example.filmsearch.BuildConfig
import com.example.filmsearch.ui.main.model.rest.FilmRepo

class RepositoryImpl : Repository {



    override fun getFilmFromServer(id: Long): FilmDTO {
       return FilmRepo.api.getFilm(id, BuildConfig.FILM_API_KEY).execute().body()!!
    }

    override fun getPopularFilmFromServer(): FilmsList {
        return FilmRepo.api.getPopularFilm(BuildConfig.FILM_API_KEY).execute().body()!!
    }


}
