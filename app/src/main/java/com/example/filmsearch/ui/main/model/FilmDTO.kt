package com.example.filmsearch.ui.main.model

data class FilmDTO(
    val genres: List<Genres>,
    val title: String?,
    val id: Long?,
    val original_language: String?,
    val releaseDate: String?,
    val overview: String?,
    val poster_path: String?,
    ) {
    data class Genres(
        val id: Long?,
        val name: String?
    )
}
