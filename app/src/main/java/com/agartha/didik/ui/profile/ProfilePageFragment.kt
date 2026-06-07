package com.agartha.didik.ui.profile

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.agartha.didik.databinding.FragmentProfilePageBinding
import com.agartha.didik.ui.auth.LoginActivity
import com.agartha.didik.ui.review.ReviewViewModel // 🌟 SELESAI: Kita panggil alamat rumah aslinya di sini, Nad!

class ProfilePageFragment : Fragment() {

    private var _binding: FragmentProfilePageBinding? = null
    private val binding get() = _binding!!
    private lateinit var viewModel: ReviewViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentProfilePageBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // 1. Ambil data dari SharedPreferences
        val sharedPref = requireActivity().getSharedPreferences("UserPrefs", Context.MODE_PRIVATE)
        val name = sharedPref.getString("user_name", "User")
        val email = sharedPref.getString("reg_email", "user@example.com")

        binding.tvNameLarge.text = name
        binding.tvEmailLarge.text = email

        // 2. Inisialisasi ViewModel secara aman pakai .get()
        viewModel = ViewModelProvider(requireActivity()).get(ReviewViewModel::class.java)

        viewModel.reviews.observe(viewLifecycleOwner) { reviews ->
            val myReviews = reviews.filter { it.reviewerName.toString() == name }
            binding.tvReviewCount.text = myReviews.size.toString()
        }

        // 3. Tombol Logout (Menendang ke LoginActivity baru kamu)
        binding.btnLogout.setOnClickListener {
            val editor = sharedPref.edit()
            editor.remove("user_name")
            editor.apply()

            // Karena sistem auth kamu sudah Activity, kita pindah pakai Intent & Clear Task
            val intent = Intent(requireContext(), LoginActivity::class.java).apply {
                flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            }
            startActivity(intent)
            requireActivity().finish() // Tutup MainActivity-nya juga biar bersih

            Toast.makeText(requireContext(), "Logged out successfully", Toast.LENGTH_SHORT).show()
        }

        // 4. Tombol Settings
        binding.btnSettings.setOnClickListener {
            Toast.makeText(requireContext(), "Settings coming soon!", Toast.LENGTH_SHORT).show()
        }

        // 5. Tombol Help
        binding.btnHelp.setOnClickListener {
            Toast.makeText(requireContext(), "Help Center coming soon!", Toast.LENGTH_SHORT).show()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}