package com.example.filmsearch.ui.main.view

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.filmsearch.R
import com.example.filmsearch.ui.main.model.database.HistoryEntity
import kotlinx.android.synthetic.main.item_history.view.*
import java.text.SimpleDateFormat
import java.util.*

class HistoryAdapter : RecyclerView.Adapter<HistoryAdapter.ViewHolder>() {

    private var data: List<HistoryEntity> = arrayListOf()

    internal fun setData(data: List<HistoryEntity>) {
        this.data = data
        notifyDataSetChanged()
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
                fun bind(data: HistoryEntity){
                    if(layoutPosition != RecyclerView.NO_POSITION){
                        with(itemView){
                            ID.text = data.id.toString()
                            title.text = data.title
                            date.text = SimpleDateFormat("yyyy-MM-dd HH.mm.ssZ").format(Date(data.timestamp))
                            notes.text = data.notes
                            Log.d("111", "имя ${data.title}")
                        }
                    }
                }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            LayoutInflater.from(parent.context)
                .inflate(R.layout.item_history, parent, false) as View
        )
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(data[position])
    }

    override fun getItemCount(): Int = data.size

}