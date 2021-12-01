package com.example.filmsearch.ui.main.model.retrofit

import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import java.util.concurrent.TimeUnit

object ApiUtils  {
    private val baseUrlMainPart = "https://api.themoviedb.org/"
    private val baseUrlVersion = "3/"
    val baseUrl = "$baseUrlMainPart$baseUrlVersion"

    fun getOkHTTPBuilderWithHeaders(): OkHttpClient{
        val httpClient = OkHttpClient.Builder()

        httpClient.connectTimeout(10, TimeUnit.SECONDS)
        httpClient.readTimeout(10, TimeUnit.SECONDS)
        httpClient.writeTimeout(10, TimeUnit.SECONDS)

        val logging = HttpLoggingInterceptor()
            .setLevel(HttpLoggingInterceptor.Level.BODY)

        httpClient.addInterceptor(logging)
        return httpClient.build()
    }
}