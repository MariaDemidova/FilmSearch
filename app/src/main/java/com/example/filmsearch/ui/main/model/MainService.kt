package com.example.filmsearch.ui.main.model

import android.app.IntentService
import android.content.Intent
import android.util.Log
import com.example.filmsearch.BuildConfig
import com.google.gson.Gson
import java.io.BufferedReader
import java.io.InputStreamReader
import java.net.MalformedURLException
import java.net.URL
import java.util.stream.Collectors
import javax.net.ssl.HttpsURLConnection

const val MAIN_SERVICE_STRING_EXTRA = "MeinServiceExtra"
const val FILM_ID = "FilmId"

class MainService(name: String = "MainService") : IntentService(name) {
    override fun onHandleIntent(intent: Intent?) {

        Log.d("MainService", "onHandleIntent ${intent?.getStringExtra(MAIN_SERVICE_STRING_EXTRA)}")

        intent?.let {
            val filmId = intent.getIntExtra(FILM_ID, 0)

            if (filmId == 0) {
                onEmptyData()
            } else {
                loadFilm(filmId)
            } ?: onEmptyIntent()
        }
    }

    private fun loadFilm(filmId: Int) {
        var urlConnection: HttpsURLConnection? = null
        val uri = try {

            URL("https://api.themoviedb.org/3/movie/${filmId}?api_key=${BuildConfig.FILM_API_KEY}")

        } catch (e: MalformedURLException) {
            onMalformedURL()
            return
        }


        try {
            urlConnection = uri.openConnection() as HttpsURLConnection
            urlConnection.apply {
                requestMethod = "GET"
                readTimeout = 10000
                addRequestProperty("key_API", BuildConfig.FILM_API_KEY)
            }
            val reader = BufferedReader(InputStreamReader(urlConnection.inputStream))
            val result = reader.lines().collect(Collectors.joining("\n"))

            val filmDTO: FilmDTO = Gson().fromJson(result, FilmDTO::class.java)

            onResponse(filmDTO)

        } catch (e: Exception) {
            onErrorResponse(e.message ?: "Unknown error")
            Log.e("", "FAILED", e)
        } finally {
            urlConnection?.disconnect()
        }
    }

    private fun onMalformedURL() {
        TODO("Not yet implemented")
    }

    private fun onErrorResponse(message: String?) {
        TODO("Not yet implemented")
    }

    private fun onResponse(filmDTO: FilmDTO) {
       filmDTO.id?.let {
           onSuccessResponse(it)
       } ?: onEmptyResponse()

        val title = filmDTO.title
        val overview = filmDTO.overview
        val releaseDate = filmDTO.releaseDate
    }

    private fun onEmptyResponse() {
        TODO("Not yet implemented")
    }

    private fun onSuccessResponse(it: Long) {
        TODO("Not yet implemented")
    }

    private fun onEmptyData() {
        TODO("Not yet implemented")
    }

    private fun onEmptyIntent() {
        TODO("Not yet implemented")
    }
}