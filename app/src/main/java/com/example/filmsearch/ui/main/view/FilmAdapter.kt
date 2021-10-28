package com.example.filmsearch.ui.main.view

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import coil.load
import coil.transform.GrayscaleTransformation
import com.example.filmsearch.BuildConfig
import com.example.filmsearch.R
import com.example.filmsearch.ui.main.model.FilmDTO
import kotlinx.android.synthetic.main.item_film.view.*

class FilmAdapter : RecyclerView.Adapter<FilmAdapter.ViewHolder>() {

    var filmData: List<FilmDTO> = listOf()

    fun setFilm(data: List<FilmDTO>) {
        filmData = data
        notifyDataSetChanged()
    }

    var listener: OnItemViewOnClickListener? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder =
        ViewHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.item_film, parent, false)
        )

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(filmData[position])
    }

    override fun getItemCount(): Int = filmData.size

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bind(film: FilmDTO) {
            itemView.apply {
                title.text = film.title
//                ganre.text = film.genres
                date.text = film.releaseDate
                setOnClickListener {
                    listener?.onItemClick(film)
                }
                imageView.load("https://image.tmdb.org/t/p/w500${film.posterPath}?api_key=${BuildConfig.FILM_API_KEY}") {
                    crossfade(true)
                    transformations(GrayscaleTransformation())
                }

            }
        }
    }

    internal fun setFilmList(filmList: List<FilmDTO>) {
        this.filmData = filmList
        notifyDataSetChanged()
    }

    fun interface OnItemViewOnClickListener {
        fun onItemClick(film: FilmDTO)
    }

}

