package com.example.filmsearch.ui.main.model.rest

import com.example.filmsearch.ui.main.model.FilmDTO
import com.example.filmsearch.ui.main.model.FilmsList
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface FilmAPI {

    @GET("discover/movie?primary_release_date.gte=2014-09-15&primary_release_date.lte=2014-10-22")
    fun getPopularFilm(

        @Query("api_key") apiKey: String
    ): Call<FilmsList>

    @GET("movie/{id}")
    fun getFilm(
        @Path("id") id: Long,
        @Query("api_key") apiKey: String
    ): Call<FilmDTO>
}