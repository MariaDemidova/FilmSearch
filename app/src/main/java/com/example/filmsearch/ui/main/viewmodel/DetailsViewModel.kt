package com.example.filmsearch.ui.main.viewmodel


import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.filmsearch.BuildConfig
import com.example.filmsearch.ui.main.model.Film
import com.example.filmsearch.ui.main.model.FilmDTO
import com.example.filmsearch.ui.main.model.RemoteDataSource
import com.example.filmsearch.ui.main.model.database.HistoryEntity
import com.example.filmsearch.ui.main.model.repos.DetailsRepository
import com.example.filmsearch.ui.main.model.repos.DetailsRepositoryImpl
import com.example.filmsearch.ui.main.model.repos.LocalRepository
import com.example.filmsearch.ui.main.model.repos.LocalRepositoryImpl
import com.example.filmsearch.ui.main.view.App
import com.google.gson.Gson
import okhttp3.Call
import okhttp3.Callback
import okhttp3.Response
import java.io.IOException
import java.text.ParseException
import java.util.*

class DetailViewModel() : ViewModel(), LifecycleObserver {
    private val repository: DetailsRepository =
        DetailsRepositoryImpl(RemoteDataSource())

    private val localRepository: LocalRepository =
        LocalRepositoryImpl(App.getHistoryDao())

    private val detailLiveData = MutableLiveData<AppState>()

    val liveData: LiveData<AppState> = detailLiveData

    fun getFilmFromRemoteDataSource(film: Film) {
        detailLiveData.value = AppState.Loading

        repository.getFilmByOkHttp("https://api.themoviedb.org/3/movie/${film.id}?api_key=${BuildConfig.FILM_API_KEY}&language=ru-RU",
            object : Callback {
                override fun onFailure(call: Call, e: IOException) {
                    detailLiveData.postValue(AppState.Error(e))
                }

                override fun onResponse(call: Call, response: Response) {
                    response.body()?.string()?.let { body ->

                        detailLiveData.postValue(checkResponse(body))

                    }
                }
            })
    }

    private fun checkResponse(response: String): AppState {

        val filmDTO = Gson().fromJson(response, FilmDTO::class.java)
        return if (filmDTO.id != null) {

            AppState.Success(
                listOf(
                    Film(
                        name = filmDTO.title ?: "",
                        id = filmDTO.id,       //переделать в фильм ид лонг
                        date = filmDTO.release_date ?: "",
                        genre = getGenres(filmDTO.genres),
                        description = filmDTO.overview ?: "",
                        posterPath = filmDTO.poster_path ?: "",
                        note = ""

                    )
                )
            )
        } else {
            AppState.Error(ParseException("Не смог распарсить json", 0))
        }
    }

    private fun getGenres(genres: List<FilmDTO.GenresDTO?>): String {
        val genresString = StringBuilder()
        genres.forEach {
            genresString.append("${it?.name}, ") // добавить
        }
        return genresString.toString()
    }

    fun saveFilm(film: Film, note: String) {
        localRepository.saveEntity(
            HistoryEntity(
                id = 0,
                title = film.name,
                timestamp = Date().time,
                notes = note

            )
        )
    }
}