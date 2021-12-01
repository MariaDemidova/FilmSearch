package com.example.filmsearch.ui.main.view

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import com.example.filmsearch.R
import com.example.filmsearch.databinding.DetailFragmentBinding
import com.example.filmsearch.databinding.FragmentHistoryBinding
import com.example.filmsearch.ui.main.viewmodel.DetailViewModel
import com.example.filmsearch.ui.main.viewmodel.HistoryViewModel
import org.koin.androidx.scope.lifecycleScope

class HistoryFragment : Fragment() {

    private val viewModel: HistoryViewModel by lazy { //создаем вью модель
        ViewModelProvider(this).get(HistoryViewModel::class.java)
    }

    private var _binding: FragmentHistoryBinding? = null
    private val binding get() = _binding!!

    private val adapter: HistoryAdapter by lazy { HistoryAdapter() }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        val view = inflater.inflate(R.layout.fragment_history, container, false)

        _binding = FragmentHistoryBinding.bind(view)
        return binding.root
    }



    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.recyclerView.adapter = adapter

        Thread{
            val listForAdapter = viewModel.getAllHistory()
            requireActivity().runOnUiThread{
                adapter.setData(listForAdapter)
            }
        }.start()


    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}