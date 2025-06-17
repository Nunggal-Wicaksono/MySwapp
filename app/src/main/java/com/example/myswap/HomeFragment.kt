package com.example.myswap

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.myswap.adapter.SwapHistoryAdapter
import com.example.myswap.viewmodel.HomeViewModel
import com.example.myswap.viewmodel.HomeViewModelFactory

class HomeFragment : Fragment() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var viewModel: HomeViewModel

    @SuppressLint("MissingInflatedId")
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_home, container, false)

        // Inisialisasi tombol-tombol
        val btnScan = view.findViewById<ImageButton>(R.id.btnScan)
        val btnTopUp = view.findViewById<ImageButton>(R.id.btnTopUp)
        val btnSend = view.findViewById<ImageButton>(R.id.btnSend)

        btnScan.setOnClickListener {
            Toast.makeText(requireContext(), "Fitur Scan akan datang!", Toast.LENGTH_SHORT).show()
        }

        btnTopUp.setOnClickListener {
            Toast.makeText(requireContext(), "Top Up ditekan", Toast.LENGTH_SHORT).show()
        }

        btnSend.setOnClickListener {
            Toast.makeText(requireContext(), "Send ditekan", Toast.LENGTH_SHORT).show()
        }

        // Setup RecyclerView
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
