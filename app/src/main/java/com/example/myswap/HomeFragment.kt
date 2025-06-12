package com.example.myswap

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.Toast
import androidx.fragment.app.Fragment

class HomeFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate layout fragment_home.xml
        val view = inflater.inflate(R.layout.fragment_home, container, false)

        // Inisialisasi tombol-tombol
        val btnScan = view.findViewById<ImageButton>(R.id.btnScan)
        val btnTopUp = view.findViewById<ImageButton>(R.id.btnTopUp)
        val btnSend = view.findViewById<ImageButton>(R.id.btnSend)

        // Aksi tombol Scan
        btnScan.setOnClickListener {
            Toast.makeText(requireContext(), "Fitur Scan akan datang!", Toast.LENGTH_SHORT).show()
            // TODO: Tambahkan intent ke fitur scan (jika ada)
        }

        // Aksi tombol Top Up
        btnTopUp.setOnClickListener {
            Toast.makeText(requireContext(), "Top Up ditekan", Toast.LENGTH_SHORT).show()
            // TODO: Tambahkan intent atau navigasi ke fitur Top Up
        }

        // Aksi tombol Send
        btnSend.setOnClickListener {
            Toast.makeText(requireContext(), "Send ditekan", Toast.LENGTH_SHORT).show()
            // TODO: Tambahkan intent atau navigasi ke fitur Send
        }

        return view
    }
}
