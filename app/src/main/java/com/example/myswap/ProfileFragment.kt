package com.example.myswap

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.myswap.databinding.FragmentProfileBinding
import com.example.myswap.util.SessionManager

class ProfileFragment : Fragment() {

    private lateinit var binding: FragmentProfileBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentProfileBinding.inflate(inflater, container, false)

        val sessionManager = SessionManager(requireContext())
        val userName = sessionManager.getName()

        // ✅ Tampilkan nama di TextView profil
        binding.tvProfileName.text = userName

        // Navigasi ke Edit Profile
        binding.btnProfile.setOnClickListener {
            startActivity(Intent(requireContext(), EditProfileActivity::class.java))
        }

        // Navigasi ke HistorisFragment
        binding.btnHistory.setOnClickListener {
            parentFragmentManager.beginTransaction()
                .replace(R.id.fragment_container, HistorisFragment())
                .addToBackStack(null)
                .commit()
        }

        // Navigasi ke Settings
        binding.btnSetting.setOnClickListener {
            startActivity(Intent(requireContext(), SettingsActivity::class.java))
        }

        // ✅ Fungsi Logout
        binding.btnLogout.setOnClickListener {
            sessionManager.clearSession()

            // Kembali ke halaman login dan hapus semua activity sebelumnya
            val intent = Intent(requireContext(), LoginActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            startActivity(intent)
        }

        return binding.root
    }
}

