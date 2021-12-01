package com.example.filmsearch.ui.main.model.retrofit

import com.example.filmsearch.ui.main.model.FilmDTO
import com.example.filmsearch.ui.main.model.FilmModel
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface FilmApi {
    @GET("/3/discover/movie?sort_by=popularity.desc") //говорим, это будет запрос гет по такому-то пути
    fun getPopularFilm(
        @Query("api_key") apiKey: String, //параметры
    @Query("language") lanq: String
    ): Call<FilmModel> //то, что нам вернется

    @GET("movie/{id}")
    fun getFilm(
        @Path("id") id: Long,
        @Query("api_key") apiKey: String
    ): Call<FilmDTO>

}