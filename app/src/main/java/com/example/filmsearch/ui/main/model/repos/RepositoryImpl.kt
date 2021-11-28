package com.example.filmsearch.ui.main.model.repos

import com.example.filmsearch.ui.main.model.*
import retrofit2.Callback

class RepositoryImpl(private val remoteDataSource: RemoteDataSource) : Repository{
    override fun getFilmFromServer(): Film = Film()

    override fun getPopularFilmsByRetro(callback: Callback<FilmModel>) {
        remoteDataSource.getPopularFilmsFromRemoteDataSource(callback)
    }
}