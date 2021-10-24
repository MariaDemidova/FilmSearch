package com.example.filmsearch.ui.main.view

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import com.example.filmsearch.databinding.DetailFragmentBinding
import com.example.filmsearch.ui.main.model.Film
import com.example.filmsearch.ui.main.model.FilmDTO
import com.example.filmsearch.ui.main.services.DetailService
import com.example.filmsearch.ui.main.services.FILM_ID_EXTRA

const val DETAILS_INTENT_FILTER = "DETAILS INTENT FILTER"
const val DETAILS_LOAD_RESULT_EXTRA = "LOAD RESULT"
const val DETAILS_INTENT_EMPTY_EXTRA = "INTENT IS EMPTY"
const val DETAILS_DATA_EMPTY_EXTRA = "DATA IS EMPTY"
const val DETAILS_RESPONSE_EMPTY_EXTRA = "RESPONSE IS EMPTY"
const val DETAILS_REQUEST_ERROR_EXTRA = "REQUEST ERROR"
const val DETAILS_REQUEST_ERROR_MESSAGE_EXTRA = "REQUEST ERROR MESSAGE"
const val DETAILS_URL_MALFORMED_EXTRA = "URL MALFORMED"
const val DETAILS_RESPONSE_SUCCESS_EXTRA = "RESPONSE SUCCESS"
const val DETAILS_TITLE_EXTRA = "DETAILS_TITLE"
const val DETAILS_OVERVIEW_EXTRA = "DETAILS_OVERVIEW"
const val DETAILS_RELEASE_DATA_EXTRA = "RELEASE_DATA"

//private const val DETAILS_INVALID = -100
//private const val FEELS_LIKE_INVALID = -100
private const val PROCESS_ERROR = "Обработка ошибки"

class DetailFragment : Fragment() {

    //    companion object {
//        private lateinit var filmBundle: Film
//        const val FILM_EXTRA = "FILM_EXTRA"
//
//        fun newInstance(bundle: Bundle): DetailFragment = DetailFragment().apply {
//            arguments = bundle
//        }
//    }
    companion object {

        const val FILM_EXTRA = "film"

        fun newInstance(bundle: Bundle): DetailFragment {
            val fragment = DetailFragment()
            fragment.arguments = bundle
            return fragment
        }
    }

    private lateinit var filmBundle: Film

    private val loadResultsReceiver: BroadcastReceiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context, intent: Intent) {
            when (intent.getStringExtra(DETAILS_LOAD_RESULT_EXTRA)) {
                DETAILS_INTENT_EMPTY_EXTRA -> TODO(PROCESS_ERROR)
                DETAILS_DATA_EMPTY_EXTRA -> TODO(PROCESS_ERROR)
                DETAILS_RESPONSE_EMPTY_EXTRA -> TODO(PROCESS_ERROR)
                DETAILS_REQUEST_ERROR_EXTRA -> TODO(PROCESS_ERROR)
                DETAILS_REQUEST_ERROR_MESSAGE_EXTRA -> TODO(PROCESS_ERROR)
                DETAILS_URL_MALFORMED_EXTRA -> TODO(PROCESS_ERROR)
                DETAILS_RESPONSE_SUCCESS_EXTRA -> renderData(
                    FilmDTO(
                        listOf(),
                        intent.getStringExtra(DETAILS_TITLE_EXTRA),
                        0,
                        "",
                        intent.getStringExtra(
                            DETAILS_RELEASE_DATA_EXTRA
                        ),
                        intent.getStringExtra(DETAILS_OVERVIEW_EXTRA),
                        ""
//                            intent.getStringExtra(
//                                DETAILS_TITLE_EXTRA),
//                            intent.getStringExtra(DETAILS_OVERVIEW_EXTRA),
//                            intent.getStringExtra(
//                                DETAILS_RELEASE_DATA_EXTRA
                    )

                )
                else -> TODO(PROCESS_ERROR)
            }
        }
    }


    private var _binding: DetailFragmentBinding? = null
    private val binding get() = _binding!!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        context?.let {
            LocalBroadcastManager.getInstance(it)
                .registerReceiver(loadResultsReceiver, IntentFilter(DETAILS_INTENT_FILTER))
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = DetailFragmentBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        filmBundle = arguments?.getParcelable(FILM_EXTRA) ?: Film()
        getFilm()


//        .let { film ->
//
//            FilmLoader(film.id, object : FilmLoader.FilmLoaderListener {
//                override fun onLoaded(filmDTO: FilmDTO) {
//                    requireActivity().runOnUiThread {
//                        displayFilm(filmDTO)
//                    }
//                }
//
//                override fun onFailed(throwable: Throwable) {
//                    requireActivity().runOnUiThread {
//                        Toast.makeText(
//                            requireContext(),
//                            throwable.localizedMessage,
//                            Toast.LENGTH_LONG
//                        ).show()
//                    }
//                }
//            }).goToInternet()
//        }
    }

    private fun getFilm() {
        binding.mainViewDetail.visibility = View.GONE
        //  binding.loadingLayout.visibility = View.VISIBLE
        context?.let {
            it.startService(Intent(it, DetailService::class.java).apply {
                Log.d("gopa", "${filmBundle.id}")
                putExtra(
                    FILM_ID_EXTRA,
                    filmBundle.id
                )
            })
        }
    }


    override fun onDestroy() {
        context?.let {
            LocalBroadcastManager.getInstance(it).unregisterReceiver(loadResultsReceiver)
        }
        super.onDestroy()
    }

    private fun displayFilm(film: FilmDTO) {
        with(binding) {
            detailDescription.text = film.overview
            detailFilmName.text = film.title
            //  detailGanre.text = film.genres[]
            detailDate.text = film.releaseDate
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun renderData(filmDTO: FilmDTO) {

        binding.mainViewDetail.visibility = View.VISIBLE
        //binding.loadingLayout.visibility = View.GONE

//        val filmDTO = filmDTO
        binding.detailFilmName.text = filmDTO.title
        binding.detailDate.text = filmDTO.releaseDate
        binding.detailDescription.text = filmDTO.overview
        // binding.o.text = filmDTO.

//        ganre.text = film.ganre
//        date.text = film.date.toString()
//
//        val fact = weatherDTO.fact
//        val temp = fact!!.temp
//        val feelsLike = fact.feels_like
//        val condition = fact.condition
//        if (temp == TEMP_INVALID || feelsLike == FEELS_LIKE_INVALID || condition == null) {
//            TODO(PROCESS_ERROR)
//        } else {
//            val city = weatherBundle.city
//            binding.cityName.text = city.city
//            binding.cityCoordinates.text = String.format(
//                getString(R.string.city_coordinates),
//                city.lat.toString(),
//                city.lon.toString()
//            )
//            binding.temperatureValue.text = temp.toString()
//            binding.feelsLikeValue.text = feelsLike.toString()
//            binding.weatherCondition.text = condition
//        }
    }


}