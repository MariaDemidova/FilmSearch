package com.example.filmsearch.ui.main.model

import android.os.Parcelable
import com.example.filmsearch.R
import kotlinx.android.parcel.Parcelize

const val defaultName: String = "Петушки"
const val defaultGanre: String = "horror"
const val defaultDate: Int = 2021
const val defaultDescription: String = "Описание отсутствует"

@Parcelize
data class Film(
    var name: String = defaultName,
    var ganre: String = defaultGanre,
    var date: Int = defaultDate,
    var imageIndex: Int = 0,
    var description: String = defaultDescription
) : Parcelable

fun getRussianFilm(): List<Film> = listOf(
    Film(imageIndex = R.drawable.pi2hi),
    Film("Старая кошатница", "musical", 1950, R.drawable.kosh),
    Film("Крокодилл против саранчи", "horror", 2021, R.drawable.croc),
    Film("Доброе утро в тюрьме", "documentary", 1991, R.drawable.utro),
)

fun getWorldFilm(): List<Film> = listOf(
    Film("Give me my money", imageIndex = R.drawable.money),
    Film("Bad man", "musical", 1988, R.drawable.badman),
    Film("Oh, my", "horror", 2015, R.drawable.oh),
    Film("Lolly", "documentary", 2019, R.drawable.lolly),
)
