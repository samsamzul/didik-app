package com.agartha.didik

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.agartha.didik.databinding.FragmentWelcomeBinding

/**
 * Fragment yang menampilkan layar selamat datang (Onboarding).
 * Memberikan opsi kepada pengguna untuk memulai aplikasi dan masuk ke halaman Login.
 */
class WelcomeFragment : Fragment() {
    private var _binding: FragmentWelcomeBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inisialisasi binding untuk layout fragment_welcome
        _binding = FragmentWelcomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Logika tombol "Mulai" (Start) untuk berpindah ke LoginFragment
        binding.btnStart.setOnClickListener {
            parentFragmentManager.beginTransaction()
                .replace(R.id.fragment_container, LoginFragment())
                // Menambahkan ke backstack agar user bisa kembali ke Welcome Screen jika menekan tombol back
                .addToBackStack(null) 
                .commit()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        // Mencegah memory leak
        _binding = null
    }
}
