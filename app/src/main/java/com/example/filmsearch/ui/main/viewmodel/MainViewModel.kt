package com.example.filmsearch.ui.main.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.filmsearch.ui.main.model.Repository
import com.example.filmsearch.ui.main.model.RepositoryImpl
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class MainViewModel(
    private val liveDataToObserve: MutableLiveData<AppState> = MutableLiveData(),
    private val repositoryImpl: Repository = RepositoryImpl()
) :
    ViewModel() {

    fun getLiveData() = liveDataToObserve

    fun getPopularFilms() {
        viewModelScope.launch(Dispatchers.IO) {

            liveDataToObserve.postValue(
                AppState.Success(
                    repositoryImpl.getPopularFilmFromServer().result

                )
            )
        }
    }

//    private fun getDataFromLocalSource(isRussian: Boolean = true) {
//        liveDataToObserve.value = AppState.Loading
//
//        viewModelScope.launch (Dispatchers.IO) {
//            sleep(1000)
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
//
////        Thread {
////            sleep(500)
////
////            liveDataToObserve.postValue(
////                AppState.Success(
////                    if (isRussian) {
////                        repositoryImpl.getFilmFromLocalStorageRus()
////                    } else {
////                        repositoryImpl.getFilmFromLocalStorageWorld()
////                    }
////                )
////            )
////
////        }.start()
//    }
}
