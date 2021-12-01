package com.example.filmsearch.ui.main.view

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import coil.load
import coil.transform.GrayscaleTransformation
import com.example.filmsearch.BuildConfig
import com.example.filmsearch.R
import com.example.filmsearch.ui.main.model.Film
import com.example.filmsearch.ui.main.model.FilmDTO
import kotlinx.android.synthetic.main.item_film.view.*

class FilmAdapter : RecyclerView.Adapter<FilmAdapter.ViewHolder>() {

    var filmList: List<Film> = ArrayList()

    var listener: OnItemViewClickListener? = null

    inner class ViewHolder(itemView: View) :
        RecyclerView.ViewHolder(itemView) {
        fun bind(film: Film) {

            itemView.apply {
                title.text = film.name
                genre.text = film.genre
                date.text = film.date
                adult.text = showAdult(film.adult)
                imageView.setImageResource(film.imageIndex)
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

    private fun showAdult(adult: Boolean): String {
        return if(adult){
            "Для взрослых!"
        }else "Можно детям"
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder =
        ViewHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.item_film, parent, false)
        )

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(filmList[position])
    }

    override fun getItemCount(): Int = filmList.size

    internal fun setFilmList(filmList: List<Film>) {
        this.filmList = filmList
        notifyDataSetChanged()
    }

    fun interface OnItemViewClickListener {
        fun onItemClick(film: Film)
    }
}

