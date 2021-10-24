package com.example.filmsearch.ui.main.services

import android.app.IntentService
import android.content.Intent
import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import com.example.filmsearch.BuildConfig
import com.example.filmsearch.ui.main.model.FilmDTO
import com.example.filmsearch.ui.main.view.*
import com.google.gson.Gson
import java.io.BufferedReader
import java.io.InputStreamReader
import java.net.MalformedURLException
import java.net.URL
import java.util.stream.Collectors
import javax.net.ssl.HttpsURLConnection

const val FILM_ID_EXTRA = "Film ID"
private const val REQUEST_GET = "GET"
private const val REQUEST_TIMEOUT = 10000
private const val REQUEST_API_KEY = "FILM_API_KEY"

class DetailService(name: String = "DetailService"): IntentService(name) {
    private val broadcastIntent = Intent(DETAILS_INTENT_FILTER)

    override fun onCreate() {
        super.onCreate()
        Log.d("gopa", "onCreate")
    }

    @RequiresApi(Build.VERSION_CODES.N)
    override fun onHandleIntent(intent: Intent?) {

        val id = intent?.getIntExtra(FILM_ID_EXTRA, 0)

        Log.d("gopa", "id. $id")

        if (intent == null) {
            onEmptyIntent()
        } else {
            if (id == 0) {
                onEmptyData()
            } else {
                loadFilm(id.toString())
            }
        }
    }

    @RequiresApi(Build.VERSION_CODES.N)
    private fun loadFilm(id: String) {
        try {
            val uri =
                URL("https://api.themoviedb.org/3/movie/${id}?api_key=${BuildConfig.FILM_API_KEY}")
            lateinit var urlConnection: HttpsURLConnection
            try {
                urlConnection = uri.openConnection() as HttpsURLConnection
                urlConnection.apply {
                    requestMethod = REQUEST_GET
                    readTimeout = REQUEST_TIMEOUT
                    addRequestProperty(REQUEST_API_KEY,
                        BuildConfig.FILM_API_KEY
                    )
                }

                val filmDTO: FilmDTO =
                    Gson().fromJson(
                        getLines(BufferedReader(InputStreamReader(urlConnection.inputStream))),
                        FilmDTO::class.java
                    )
                onResponse(filmDTO)
                Log.d("gopa", "${filmDTO.toString()}")

            } catch (e: Exception) {
                onErrorRequest(e.message ?: "Empty error")
            } finally {
                urlConnection.disconnect()
            }
        } catch (e: MalformedURLException) {
            onMalformedURL()
        }
    }
    @RequiresApi(Build.VERSION_CODES.N)
    private fun getLines(reader: BufferedReader): String {
        return reader.lines().collect(Collectors.joining("\n"))
    }

    private fun onResponse(filmDTO: FilmDTO) {
        val id = filmDTO.id
        if (id == null) {
            onEmptyResponse()
        } else {
            onSuccessResponse(filmDTO.title, filmDTO.overview, filmDTO.releaseDate)
        }
    }

    private fun onSuccessResponse(title:String?, overview: String?, releaseDate: String?) {
        putLoadResult(DETAILS_RESPONSE_SUCCESS_EXTRA)
        broadcastIntent.putExtra(DETAILS_TITLE_EXTRA, title)
        broadcastIntent.putExtra(DETAILS_OVERVIEW_EXTRA, overview)
        broadcastIntent.putExtra(DETAILS_RELEASE_DATA_EXTRA, releaseDate)
        LocalBroadcastManager.getInstance(this).sendBroadcast(broadcastIntent)
    }

    private fun onMalformedURL() {
        putLoadResult(DETAILS_URL_MALFORMED_EXTRA)
        LocalBroadcastManager.getInstance(this).sendBroadcast(broadcastIntent)
    }

    private fun onErrorRequest(error: String) {
        putLoadResult(DETAILS_REQUEST_ERROR_EXTRA)
        broadcastIntent.putExtra(DETAILS_REQUEST_ERROR_MESSAGE_EXTRA, error)
        LocalBroadcastManager.getInstance(this).sendBroadcast(broadcastIntent)
    }

    private fun onEmptyResponse() {
        putLoadResult(DETAILS_RESPONSE_EMPTY_EXTRA)
        LocalBroadcastManager.getInstance(this).sendBroadcast(broadcastIntent)
    }

    private fun onEmptyIntent() {
        putLoadResult(DETAILS_INTENT_EMPTY_EXTRA)
        LocalBroadcastManager.getInstance(this).sendBroadcast(broadcastIntent)
    }

    private fun onEmptyData() {
        putLoadResult(DETAILS_DATA_EMPTY_EXTRA)
        LocalBroadcastManager.getInstance(this).sendBroadcast(broadcastIntent)
    }

    private fun putLoadResult(result: String) {
        broadcastIntent.putExtra(DETAILS_LOAD_RESULT_EXTRA, result)

    }

}