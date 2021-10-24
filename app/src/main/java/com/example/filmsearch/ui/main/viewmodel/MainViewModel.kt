package com.example.filmsearch.ui.main.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.filmsearch.ui.main.model.Repository
import com.example.filmsearch.ui.main.model.RepositoryImpl
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.lang.Thread.sleep

class MainViewModel(
    private val liveDataToObserve: MutableLiveData<AppState> = MutableLiveData(),
    private val repositoryImpl: Repository = RepositoryImpl()) :
    ViewModel() {

    fun getLiveData() = liveDataToObserve

//    private val repository: Repository = RepositoryImpl()
//    val liveData: LiveData<AppState> = liveDataToObserve

    fun getFilmFromLocalSourceRus() = getDataFromLocalSource(isRussian=true)
    fun getFilmFromLocalSourceWorld() = getDataFromLocalSource(isRussian = false)
    fun getFilmFromRemoteSource() = getDataFromLocalSource(isRussian = true)

    private fun getDataFromLocalSource(isRussian: Boolean = true) {
        liveDataToObserve.value = AppState.Loading

//        viewModelScope.launch (Dispatchers.IO) {
//            sleep(500)
//            liveDataToObserve.postValue(
//                AppState.Success(
//                    if (isRussian) {
//                        repositoryImpl.getFilmFromLocalStorageRus()
//                    } else {
//                        repositoryImpl.getFilmFromLocalStorageWorld()
//                    }
//                )
//            )
//        }

        Thread {
            sleep(500)

            liveDataToObserve.postValue(
                AppState.Success(
                    if (isRussian) {
                        repositoryImpl.getFilmFromLocalStorageRus()
                    } else {
                        repositoryImpl.getFilmFromLocalStorageWorld()
                    }
                )
            )

        }.start()
    }
}
