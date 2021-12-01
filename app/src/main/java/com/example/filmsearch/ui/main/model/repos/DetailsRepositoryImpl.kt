package com.example.filmsearch.ui.main.model.repos

import com.example.filmsearch.ui.main.model.RemoteDataSource
import okhttp3.Callback

class DetailsRepositoryImpl (private val remoteDataSource: RemoteDataSource): DetailsRepository { //отвечает за получение данных с сервера
    override fun getFilmByOkHttp(requestLink: String, callback: Callback) {
       remoteDataSource.getFilmFromRemoteDataSourceByOk(requestLink, callback)
    }
}