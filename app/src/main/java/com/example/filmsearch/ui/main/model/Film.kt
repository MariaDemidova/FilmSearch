package com.example.filmsearch.ui.main.model

import android.os.Parcelable
import com.example.filmsearch.R
import kotlinx.android.parcel.Parcelize

const val defaultName: String = "Test"
const val defaultGanre: String = "genre_test"
const val defaultDate: Int = 2021
const val defaultId: Int = 550
const val defaultDescription: String = "Описание отсутствует"

@Parcelize
data class Film(

    var name: String = defaultName,
    var ganre: String = defaultGanre,
    var date: Int = defaultDate,
    var imageIndex: Int = 0,
    var id: Int = defaultId,
    var description: String = defaultDescription

) : Parcelable

fun getRussianFilm(): List<Film> = listOf(
    Film("Ночной дозор", "fantasy", 2004, R.drawable.nochnoidozor, id = 3040),
    Film("Все умрут, а я останусь", "drama", 2008, R.drawable.vseumrut, id = 31418),
    Film("Похороните меня за плинтусом", "drama", 2009, R.drawable.pohoronite, id = 19875)

//    Film(imageIndex = R.drawable.pi2hi),
//    Film("Старая кошатница", "musical", 1950, R.drawable.kosh, 834404),
//    Film("Крокодилл против саранчи", "horror", 2021, R.drawable.croc),
//    Film("Доброе утро в тюрьме", "documentary", 1991, R.drawable.utro),
)

fun getWorldFilm(): List<Film> = listOf(

    Film("Золушка", "fantasy", 2021, R.drawable.zolushka, id = 593910),
    Film("Видеть", "drama", 2019, R.drawable.videt, id = 80752),
    Film("Ведьмы", "fantasy", 2020, R.drawable.vedmi, id = 531219)
//    Film("Give me my money", imageIndex = R.drawable.money),
//    Film("Bad man", "musical", 1988, R.drawable.badman),
//    Film("Oh, my", "horror", 2015, R.drawable.oh),
//    Film("Lolly", "documentary", 2019, R.drawable.lolly),
)
