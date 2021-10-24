package com.example.filmsearch.ui.main.view

import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.filmsearch.R
import com.example.filmsearch.databinding.DetailFragmentBinding
import com.example.filmsearch.databinding.MainFragmentBinding
import com.example.filmsearch.ui.main.model.*
import com.example.filmsearch.ui.main.viewmodel.MainViewModel
import com.example.filmsearch.ui.main.viewmodel.AppState
import com.google.gson.Gson
import kotlinx.android.synthetic.main.item_film.*
import kotlinx.android.synthetic.main.main_fragment.*
import java.io.BufferedReader
import java.io.InputStreamReader
import java.net.URI
import java.net.URL
import java.net.URLConnection
import java.util.stream.Collector
import java.util.stream.Collectors
import javax.net.ssl.HttpsURLConnection

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

            FilmLoader(film.id, object : FilmLoader.FilmLoaderListener {
                override fun onLoaded(filmDTO: FilmDTO) {
                    requireActivity().runOnUiThread {
                        displayFilm(filmDTO)
                    }
                }

                override fun onFailed(throwable: Throwable) {
                    requireActivity().runOnUiThread {
                        Toast.makeText(
                            requireContext(),
                            throwable.localizedMessage,
                            Toast.LENGTH_LONG
                        ).show()
                    }

                }

            }).goToInternet()

        }

    }

    private fun displayFilm(film: FilmDTO) {
        with(binding) {
            detailDescription.text = film.overview
            detailFilmName.text = film.title
            //  detailGanre.text = film.genres[]
            detailDate.text = film.release_date
        }
    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}