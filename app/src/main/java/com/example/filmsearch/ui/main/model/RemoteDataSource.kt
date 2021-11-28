package com.example.filmsearch.ui.main.model

import com.example.filmsearch.BuildConfig
import com.example.filmsearch.ui.main.model.retrofit.ApiUtils
import com.example.filmsearch.ui.main.model.retrofit.FilmApi
import com.google.gson.GsonBuilder
import okhttp3.OkHttpClient
import okhttp3.Request
import retrofit2.Callback
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class RemoteDataSource { //то, откуда будем получать данные (отвечает за получение данных)
    ///////////для ретрофита
    private val filmApi = Retrofit.Builder() // создали интерфейс, а билдер реализует его за нас
        .baseUrl(ApiUtils.baseUrl)
        .addConverterFactory(
            GsonConverterFactory.create(
                GsonBuilder().setLenient().create()
            )
        )
        .client(ApiUtils.getOkHTTPBuilderWithHeaders())
        .build().create(FilmApi::class.java)


    ///////////для окхттп
    fun getFilmFromRemoteDataSourceByOk(link: String, callback: okhttp3.Callback) { //import okhttp3.Callback!!!
        val client = OkHttpClient() //для окхтпп

        val request = Request.Builder()
            .url(link) //"https://api.themoviedb.org/3/movie/${film.id}?api_key=${BuildConfig.FILM_API_KEY}&language=ru-RU"
            .addHeader("key_API", BuildConfig.FILM_API_KEY)
            .get()
            .build()

        client.newCall(request).enqueue(callback)


    }

    fun getPopularFilmsFromRemoteDataSource( callback: Callback<FilmModel>) {
        filmApi.getPopularFilm(BuildConfig.FILM_API_KEY,"ru-RU").enqueue(callback)
    }

}