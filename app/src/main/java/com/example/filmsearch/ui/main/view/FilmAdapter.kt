package com.example.filmsearch.ui.main.view

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.filmsearch.R
import com.example.filmsearch.ui.main.model.Repository
import com.example.filmsearch.ui.main.model.RepositoryImpl
import com.example.filmsearch.ui.main.model.Film
import kotlinx.android.synthetic.main.item_film.view.*

class FilmAdapter : RecyclerView.Adapter<FilmAdapter.ViewHolder>() {

    val repository: Repository = RepositoryImpl()

    var filmList: List<Film> = ArrayList()
        set(value) {
            field = value
            notifyDataSetChanged()
        }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bind(film: Film) {
            itemView.title.text = film.name
            itemView.ganre.text = film.ganre
            itemView.date.text = film.date.toString()
            itemView.imageView.setImageResource(film.imageIndex)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder =
        ViewHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.item_film, parent, false)
        )

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(filmList[position])
    }

    override fun getItemCount(): Int = repository.getFilmFromLocalStorage().size

    internal fun setFilmList (filmList : List<Film>){
        this.filmList = filmList
        notifyDataSetChanged()
    }

}

