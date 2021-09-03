package com.example.filmsearch.ui.main.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.filmsearch.ui.main.model.Repository
import com.example.filmsearch.ui.main.model.RepositoryImpl
import java.lang.Thread.sleep

class MainViewModel(private val liveDataToObserve: MutableLiveData<AppState> = MutableLiveData()) :
    ViewModel() {

    private val repository: Repository = RepositoryImpl()
     val liveData: LiveData<AppState> = liveDataToObserve

    fun getFilmFromLocalSource() = getDataFromLocalSource()
    fun getFilmFromRemoteSource() = getDataFromLocalSource()

    private fun getDataFromLocalSource() {
        liveDataToObserve.value = AppState.Loading

        Thread {
            sleep(2000)
            liveDataToObserve.postValue(AppState.Success(repository.getFilmFromLocalStorage()))

        }.start()
    }
}

