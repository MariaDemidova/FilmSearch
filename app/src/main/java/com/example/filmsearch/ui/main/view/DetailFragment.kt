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
import com.example.filmsearch.ui.main.viewmodel.MainViewModel
import com.example.filmsearch.ui.main.model.Repository
import com.example.filmsearch.ui.main.model.RepositoryImpl
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

        button.setOnClickListener {
            viewModel.liveData.observe(viewLifecycleOwner,
                { state ->
                    renderData(state)
                })
            viewModel.getFilmFromLocalSource()
        }
    }

    private fun renderData(state: AppState) {
        val repository: Repository = RepositoryImpl()
        when (state) {
            is AppState.Loading -> binding.loadingLayout.visibility = View.VISIBLE

            is AppState.Success -> {
                binding.loadingLayout.visibility = View.GONE

                filmAdapter.setFilmList(repository.getFilmFromLocalStorageRus())
                filmAdapter.let {
                    val layoutManager = LinearLayoutManager(view?.context)
                    recycler_view_lines.layoutManager =
                        layoutManager
                    recycler_view_lines.adapter = it
                    recycler_view_lines.addItemDecoration(
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
                    .setAction("Reload") { viewModel.getFilmFromLocalSource() }
                    .show()
            }
            }
        }

        override fun onDestroyView() {
            super.onDestroyView()
            _binding = null
        }
    }