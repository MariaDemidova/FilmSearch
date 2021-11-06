package com.example.filmsearch.ui.main.view

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import coil.load
import coil.transform.GrayscaleTransformation
import com.example.filmsearch.BuildConfig
import com.example.filmsearch.R
import com.example.filmsearch.databinding.DetailFragmentBinding
import com.example.filmsearch.ui.main.model.FilmDTO
import com.example.filmsearch.ui.main.viewmodel.AppState
import com.example.filmsearch.ui.main.viewmodel.DetailsViewModel
import kotlinx.android.synthetic.main.detail_fragment.*
import kotlinx.android.synthetic.main.detail_fragment.main_view_detail
import kotlinx.android.synthetic.main.main_fragment.*
import org.koin.androidx.viewmodel.ext.android.viewModel


class DetailFragment : Fragment() {

    private var _binding: DetailFragmentBinding? = null
    private val binding get() = _binding!!
    private val viewModel: DetailsViewModel by viewModel()


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

        arguments?.getParcelable<FilmDTO>(FILM_EXTRA)?.let {
            with(binding) {
                detailDescription.text = it.overview
                detailFilmName.text = it.title
                // detailGanre.text = film.genres[]
                detailDate.text = it.releaseDate
                Log.d("gopa", it.id.toString())
                viewModel.loadData(it.id!!)
            }
        }

        viewModel.liveDataToObserve.observe(viewLifecycleOwner, { appState ->

            Log.d("gopa", appState.toString())

            when (appState) {
                is AppState.Error -> {
                    main_view_detail.visibility = View.INVISIBLE
                    loadingLayout.visibility = View.GONE
                    errorTV.visibility = View.VISIBLE
                }
                AppState.Loading -> {
                    main_view_detail.visibility = View.INVISIBLE
                    loadingLayout.visibility = View.VISIBLE
                }
                is AppState.Success -> {
                    loadingLayout.visibility = View.GONE
                    main_view_detail.visibility = View.VISIBLE
                    detail_FilmName.text = appState.filmsList[0].title
                    detail_description.text = appState.filmsList[0].overview
                    //  ganre.text = appState.filmsList[0].ganre
                    detail_date.text = appState.filmsList[0].releaseDate
                    imageView.load("https://image.tmdb.org/t/p/w500${appState.filmsList[0].posterPath}?api_key=${BuildConfig.FILM_API_KEY}") {
                        crossfade(true)
                        transformations(GrayscaleTransformation())
                    }

                }
            }
        })
    }

    companion object {
        const val FILM_EXTRA = "FILM_EXTRA"
        fun newInstance(bundle: Bundle): DetailFragment = DetailFragment().apply {
            arguments = bundle
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}