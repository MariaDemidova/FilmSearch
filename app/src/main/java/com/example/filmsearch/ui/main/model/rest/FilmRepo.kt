package com.example.filmsearch.ui.main.model.rest

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.create

object FilmRepo {
    val api: FilmAPI by lazy {
        val adapter = Retrofit.Builder()
            .baseUrl(ApiUtils.baseUrl)
            .addConverterFactory(GsonConverterFactory.create())

            .client(ApiUtils.getOkHTTPBuilderWithHeaders())
            .build()

        adapter.create(FilmAPI::class.java)
    }

}
