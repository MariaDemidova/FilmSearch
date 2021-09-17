package com.example.filmsearch.ui.main.model

import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import com.example.filmsearch.BuildConfig
import com.google.gson.Gson
import java.io.BufferedReader
import java.io.InputStreamReader
import java.net.URL
import java.util.stream.Collectors
import javax.net.ssl.HttpsURLConnection

class FilmLoader(
    private val id: Int,
    private val listener: FilmLoaderListener
) {
    @RequiresApi(Build.VERSION_CODES.N)
    fun goToInternet() {

        Thread {
            val uri =
                URL("https://api.themoviedb.org/3/movie/${id}?api_key=${BuildConfig.FILM_API_KEY}")
            var urlConnection: HttpsURLConnection? = null

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

                listener.onLoaded(filmDTO)
            } catch (e: Exception) {
                listener.onFailed(e)
                Log.e("", "FAILED", e)
            } finally {
                urlConnection?.disconnect()
            }
        }.start()
    }

    interface FilmLoaderListener {
        fun onLoaded(filmDTO: FilmDTO)
        fun onFailed(throwable: Throwable)
    }
}