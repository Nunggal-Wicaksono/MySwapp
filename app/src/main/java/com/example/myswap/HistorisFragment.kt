package com.example.myswap

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.myswap.R
import com.example.myswap.adapter.SwapHistoryAdapter
import com.example.myswap.data.AppDatabase
import com.example.myswap.viewmodel.HomeViewModel
import com.example.myswap.viewmodel.HomeViewModelFactory
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class HistorisFragment : Fragment() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: SwapHistoryAdapter
    private lateinit var viewModel: HomeViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_historis, container, false)

        recyclerView = view.findViewById(R.id.recyclerHistory)
        recyclerView.layoutManager = LinearLayoutManager(requireContext())

        // ✅ Gunakan Factory agar bisa buat ViewModel dengan parameter Application
        val application = requireActivity().application
        val factory = HomeViewModelFactory(application)
        viewModel = ViewModelProvider(this, factory)[HomeViewModel::class.java]

        viewModel.history.observe(viewLifecycleOwner) { historyList ->
            recyclerView.adapter = SwapHistoryAdapter(historyList)
        }
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel = ViewModelProvider(
            this,
            ViewModelProvider.AndroidViewModelFactory.getInstance(requireActivity().application)
        )[HomeViewModel::class.java]

        viewModel.history.observe(viewLifecycleOwner) { historyList ->
            recyclerView.adapter = SwapHistoryAdapter(historyList)
        }
    }
}
