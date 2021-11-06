package com.example.filmsearch.ui.main.model

import com.google.gson.annotations.SerializedName

data class FilmsList(

    @SerializedName("page")
    val page: Int,

    @SerializedName("results")
    val result: List<FilmDTO>,
)
