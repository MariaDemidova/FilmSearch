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
import com.example.filmsearch.databinding.MainFragmentBinding
import com.example.filmsearch.ui.main.model.Film
import com.example.filmsearch.ui.main.model.Repository
import com.example.filmsearch.ui.main.model.RepositoryImpl
import com.example.filmsearch.ui.main.viewmodel.MainViewModel
import com.example.filmsearch.ui.main.viewmodel.AppState
import com.google.android.material.snackbar.Snackbar
import kotlinx.android.synthetic.main.main_fragment.*

class MainFragment : Fragment() {

    companion object {
        fun newInstance() = MainFragment()
    }

    private val filmAdapter: FilmAdapter by lazy {
        FilmAdapter()
    }

    private lateinit var viewModel: MainViewModel
    private var _binding: MainFragmentBinding? = null
    private val binding get() = _binding!!

    private var isDataSetRus: Boolean = true

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val view = inflater.inflate(R.layout.main_fragment, container, false)

        _binding = MainFragmentBinding.bind(view)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel = ViewModelProvider(this).get(MainViewModel::class.java)

        filmAdapter.listener =
            FilmAdapter.OnItemViewOnClickListener { film ->
                val manager = activity?.supportFragmentManager
                if(manager != null){
                    val bundle = Bundle()
                    bundle.putParcelable(DetailFragment.FILM_EXTRA, film)
                    manager.beginTransaction()
                        .replace(R.id.container, DetailFragment.newInstance(bundle))
                        .addToBackStack("")
                        .commit()
                }
            }

        binding.buttonLang.setOnClickListener {

            changeFilmDataSet()
        }

        viewModel.getLiveData().observe(viewLifecycleOwner,
            { state ->
                renderData(state)
            })
        viewModel.getFilmFromLocalSourceRus()

    }

    private fun changeFilmDataSet() {
        if (isDataSetRus) {
            viewModel.getFilmFromLocalSourceWorld()
            binding.buttonLang.setImageResource(R.drawable.world2)
        } else {
            viewModel.getFilmFromLocalSourceRus()
            binding.buttonLang.setImageResource(R.drawable.rus2)
        }
        isDataSetRus = !isDataSetRus
    }


    private fun renderData(state: AppState) {
        //  val repository: Repository = RepositoryImpl()
        when (state) {
            is AppState.Loading -> binding.loadingLayout.visibility = View.VISIBLE

            is AppState.Success -> {
                binding.loadingLayout.visibility = View.GONE

                filmAdapter.setFilmList(state.filmsList)
                filmAdapter.let {
                    val layoutManager = LinearLayoutManager(view?.context)
                    mainFragmentRecyclerView.layoutManager =
                        layoutManager
                    mainFragmentRecyclerView.adapter = it
                    mainFragmentRecyclerView.addItemDecoration(
                        DividerItemDecoration(
                            view?.context,
                            DividerItemDecoration.VERTICAL
                        )
                    )
                    it.notifyDataSetChanged()
                }
            }
            is AppState.Error -> {
                binding.loadingLayout.visibility = View.GONE
                Snackbar
                    .make(binding.mainView, "ERROR", Snackbar.LENGTH_INDEFINITE)
                    .setAction("Reload") { viewModel.getFilmFromLocalSourceRus() }
                    .show()
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}