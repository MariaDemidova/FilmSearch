package com.example.filmsearch.ui.main.view

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import coil.load
import coil.transform.GrayscaleTransformation
import com.example.filmsearch.BuildConfig
import com.example.filmsearch.R
import com.example.filmsearch.databinding.DetailFragmentBinding
import com.example.filmsearch.ui.main.model.Film
import com.example.filmsearch.ui.main.viewmodel.AppState
import com.example.filmsearch.ui.main.viewmodel.DetailViewModel
import kotlinx.android.synthetic.main.detail_fragment.*

class DetailFragment : Fragment() {

    companion object {
        const val FILM_EXTRA = "FILM_EXTRA"

        fun newInstance(bundle: Bundle): DetailFragment {
            val fragment = DetailFragment()
            fragment.arguments = bundle
            return fragment
        }
    }

    private val viewModel: DetailViewModel by lazy { //создаем вью модель
        ViewModelProvider(this).get(DetailViewModel::class.java)
    }

    private var _binding: DetailFragmentBinding? = null
    private val binding get() = _binding!!
    var noteString = ""

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
        val film = arguments?.getParcelable(FILM_EXTRA) ?: Film() //из бандела получаем фильм,

        viewModel.liveData.observe(viewLifecycleOwner) { //подписываемся на изменения лайф даты
            renderData(it)
        }
        viewModel.getFilmFromRemoteDataSource(film) // во вью модель говорим- сходи в интернет, он идет в инет: говорит во вюь модель репозиторию - сходи в инет

        add_note_button.setOnClickListener {
            noteString = note.text.toString()
            Thread{
                viewModel.saveFilm(film, noteString)
            }.start()
                add_note_button.text = "Сохранено"
        }
    }

    private fun renderData(state: AppState) {
        when (state) {
            is AppState.Loading -> {
                binding.loadingLayout.show()
                binding.mainViewDetail.hide()
            }

            is AppState.Success -> {
                binding.loadingLayout.hide()
                binding.mainViewDetail.show()

                val film = state.filmsList.first()

                Thread{
                    viewModel.saveFilm(film, noteString)
                }.start()
                Log.d("fff", Thread.activeCount().toString())

                noteString = ""
                with(binding) {
                    detailDate.text = film.date.toString()
                    detailName.text = film.name
                    detailGenre.text = film.genre
                    description.text = film.description
                    detailImg.load("https://image.tmdb.org/t/p/w500${film.posterPath}?api_key=${BuildConfig.FILM_API_KEY}") {
                        crossfade(true)
                        transformations(GrayscaleTransformation())
                    }
                }
            }

            is AppState.Error -> {
                binding.loadingLayout.hide()
                binding.mainViewDetail.showSnackBar(
                    "ERROR",
                    "Reload",
                    {
                        viewModel.getFilmFromRemoteDataSource(Film())
                    }
                )

            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null

    }
}