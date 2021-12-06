package com.example.filmsearch.ui.main.view

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.os.Bundle
import android.util.Log
import android.view.*
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat.checkSelfPermission
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.filmsearch.R
import com.example.filmsearch.databinding.MainFragmentBinding
import com.example.filmsearch.ui.main.utils.AlertDialogs
import com.example.filmsearch.ui.main.viewmodel.AppState
import com.example.filmsearch.ui.main.viewmodel.MainViewModel
import kotlinx.android.synthetic.main.main_fragment.*

private const val dataSetKey = "dataSetKey"

class MainFragment : Fragment() {

    companion object {
        fun newInstance() = MainFragment()
    }

    private val filmAdapter: FilmAdapter by lazy {
        FilmAdapter()
    }
    private val viewModel: MainViewModel by lazy {
        ViewModelProvider(this).get(MainViewModel::class.java)
    }

    private var _binding: MainFragmentBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val view = inflater.inflate(
            R.layout.main_fragment, container,
            false
        )

        _binding = MainFragmentBinding.bind(view)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        filmAdapter.listener =
            FilmAdapter.OnItemViewClickListener { film ->
                activity?.supportFragmentManager?.let {
                    it.beginTransaction()
                        .replace(R.id.container, DetailFragment.newInstance(Bundle().apply {
                            putParcelable(
                                DetailFragment.FILM_EXTRA,
                                film
                            )
                        }))
                        .addToBackStack("")
                        .commit()
                }
            }

        map_button.setOnClickListener {

            permissionGeoResult.launch(Manifest.permission.ACCESS_FINE_LOCATION)

            if (checkSelfPermission(
                    requireContext(),
                    Manifest.permission.ACCESS_FINE_LOCATION
                ) != PackageManager.PERMISSION_GRANTED
            ) return@setOnClickListener

        }

        button.setOnClickListener {
            viewModel.liveData.observe(viewLifecycleOwner)
            { state ->
                Log.d("fff", "$state")

                renderData(state)
            }
            viewModel.getFilmFromRemoteDataSource(isAdult())
        }

        button_adult.setOnCheckedChangeListener { buttonView, isChecked ->
            setDataSetToDisk(isChecked) //записываем тру или фолс
        }

        button_adult.isChecked = isAdult()

    }

    private val permissionGeoResult =
        registerForActivityResult(ActivityResultContracts.RequestPermission()) { result ->
            when {
                result ->
                    activity?.supportFragmentManager?.let {
                        it.beginTransaction()
                            .replace(R.id.container, MapsFragment.newInstance())
                            .addToBackStack("")
                            .commit()
                    }

                !ActivityCompat.shouldShowRequestPermissionRationale(
                    requireActivity(),
                    Manifest.permission.ACCESS_FINE_LOCATION
                ) -> {
                    AlertDialogs(requireContext()).showAlertDialog(getString(R.string.AD_Access_to_geo_title), getString(R.string.AD_Access_to_geo_mess))
                }
                else -> Toast.makeText(
                    requireContext(),
                    R.string.No_access_to_permission,
                    Toast.LENGTH_LONG
                ).show()
            }
        }

    private fun isAdult(): Boolean {
        activity?.let {
            return activity
                ?.getPreferences(Context.MODE_PRIVATE)
                ?.getBoolean(dataSetKey, true) ?: true
        }
        return false
    }

    private fun setDataSetToDisk(isAdult: Boolean) {
        val editor = activity?.getPreferences(Context.MODE_PRIVATE)?.edit()
        editor?.putBoolean(dataSetKey, isAdult)
        editor?.apply()
    }

    private fun renderData(state: AppState) {
        when (state) {
            is AppState.Loading -> {
                binding.loadingLayout.show()
            }

            is AppState.Success -> {
                binding.loadingLayout.hide()

                filmAdapter.filmList = state.filmsList

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
                binding.loadingLayout.hide()
                binding.FABButton.showSnackBar(
                    "ERROR",
                    "Reload",
                    { viewModel.getFilmFromRemoteDataSource(isAdult()) }
                )
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}