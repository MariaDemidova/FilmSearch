package com.example.filmsearch.ui.main.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.filmsearch.R
import com.example.filmsearch.databinding.DetailFragmentBinding
import com.example.filmsearch.databinding.MainFragmentBinding
import com.example.filmsearch.ui.main.model.Film
import com.example.filmsearch.ui.main.viewmodel.MainViewModel
import com.example.filmsearch.ui.main.model.Repository
import com.example.filmsearch.ui.main.model.RepositoryImpl
import com.example.filmsearch.ui.main.viewmodel.AppState
import kotlinx.android.synthetic.main.main_fragment.*

class DetailFragment : Fragment() {

    companion object {
        const val FILM_EXTRA = "FILM_EXTRA"
        fun newInstance(bundle: Bundle): DetailFragment = DetailFragment().apply {
            arguments = bundle
        }
    }

    private var _binding: DetailFragmentBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val view = inflater.inflate(R.layout.detail_fragment, container, false)

        _binding = DetailFragmentBinding.bind(view)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        arguments?.getParcelable<Film>(FILM_EXTRA)?.let { film ->
            with(binding) {
                detailDescription.text = film.description
                detailFilmName.text = film.name
                detailGanre.text = film.ganre
                detailDate.text = film.date.toString()
            }
        }
    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}