package com.example.filmsearch.ui.main.view
import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.*
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.filmsearch.R
import com.example.filmsearch.databinding.MainFragmentBinding

import com.example.filmsearch.ui.main.viewmodel.MainViewModel
import com.example.filmsearch.ui.main.viewmodel.AppState
import kotlinx.android.synthetic.main.main_fragment.*

private const val dataSetKey = "dataSetKey"
class MainFragment : Fragment() {

    companion object {
        fun newInstance() = MainFragment()
    }

    private val filmAdapter: FilmAdapter by lazy {
        FilmAdapter()
    }
    private val viewModel: MainViewModel by lazy { //создае вью модель
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

//        val toolbar = (activity as AppCompatActivity).supportActionBar
//        Log.d("fff", "${toolbar?.title}")

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

        binding.buttonAdult.setOnClickListener {
           // isAdult = !isAdult
            initDataSet()
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
            setDataSetToDisk(isChecked)
        }
        button_adult.isChecked =  isAdult()

        initDataSet()
    }

    private fun isAdult(): Boolean {
         activity?.let {
             return activity
                ?.getPreferences(Context.MODE_PRIVATE)
                ?.getBoolean(dataSetKey, true) ?: true
        }
        return false
    }

    private fun initDataSet() {
      //  if (button_adult.isChecked) {
            viewModel.getFilmFromRemoteDataSource(isAdult())
    //    } else {
     //       viewModel.getFilmFromRemoteDataSource(!isAdult())
      //  }
//        setDataSetToDisk(isAdult)

    }

    private fun setDataSetToDisk(isAdult: Boolean) {
        val editor = activity?.getPreferences(Context.MODE_PRIVATE)?.edit() //открываем на чтение
        editor?.putBoolean(dataSetKey, isAdult)
        editor?.apply() //апплай работает ассинхронно
    }


    private fun renderData(state: AppState) {
        when (state) {
            is AppState.Loading -> {
                Log.d("fff", "loading")
                binding.loadingLayout.show()
            }

            is AppState.Success -> {
                binding.loadingLayout.hide()
                Log.d("fff", "success")

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