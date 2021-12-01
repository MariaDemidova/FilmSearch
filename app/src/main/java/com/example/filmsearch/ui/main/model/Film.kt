package com.example.filmsearch.ui.main.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Film(
    var id: Long = 550,
    var name: String = "Заяц",
    var genre: String = "Ужасы",
    var date: String = "2020",
    var imageIndex: Int = 0,
    var description: String = "Нет описания",
    val posterPath: String? = "",
    val adult: Boolean = false,
    var note: String = ""
) : Parcelable



