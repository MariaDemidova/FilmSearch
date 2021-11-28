package com.example.filmsearch.ui.main.model

data class FilmDTO(
    val genres: List<GenresDTO?>,
    val title: String?,
    val id: Long?,
    val release_date: String?,
    val overview: String?,
    val poster_path: String?,
    val adult: Boolean
) {
    data class GenresDTO(
        val id: Long?,
        val name: String?
    )
}