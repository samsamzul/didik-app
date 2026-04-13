package com.agartha.didik

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.agartha.didik.data.ReviewModel
import com.agartha.didik.adapter.ReviewAdapter
import com.agartha.didik.databinding.FragmentDashboardBinding

class DashboardFragment : Fragment() {

    private var _binding: FragmentDashboardBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentDashboardBinding.inflate(inflater, container, false)
        return binding.root
    }

    @SuppressLint("SetTextI18n")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // 1. Buka "Loker" SharedPreferences yang namanya "UserPrefs"
        val sharedPref = requireActivity().getSharedPreferences("UserPrefs", android.content.Context.MODE_PRIVATE)

        // 2. Ambil data dengan kunci "user_name".
        // Kalau ternyata kosong, kita kasih default "Guest" atau "Nadilah"
        val username = sharedPref.getString("user_name", "Nadilah")

        // 3. Tampilkan ke TextView Greeting (Halo, Nadilah!)
        binding.tvGreeting.text = "Halo, $username! 👋"

        // --- Sisa kode setup RecyclerView dan Adapter kamu di bawah sini ---
        binding.rvReview.layoutManager = LinearLayoutManager(context)

        // 2. Siapkan Data Dummy (Sesuai ReviewModel: id, companyName, position, jobDesc, reviewText, rating)
        val dataReview = listOf(
            ReviewModel(1, "PT. Telkom Indonesia", "UI/UX Designer", "Designing interfaces", "Great environment", 5.0f),
            ReviewModel(2, "PT. Gojek", "Backend Developer", "Building APIs", "Fast paced", 4.5f),
            ReviewModel(3, "Shopee", "System Analyst", "Analyzing requirements", "Good benefits", 4.0f)
        )

        // 3. Pasang Adapter
        val adapter = ReviewAdapter(dataReview)
        binding.rvReview.adapter = adapter

        // 4. Logika Tombol FAB (Tambah Review)
        binding.fabAddReview.setOnClickListener {
            parentFragmentManager.beginTransaction()
                .replace(R.id.fragment_container, FormReviewFragment()) // Pastikan ID container bener
                .addToBackStack(null) // Biar kalau di-back balik ke Dashboard, bukan keluar aplikasi
                .commit()
        }

        binding.ivProfile.setOnClickListener {
            parentFragmentManager.beginTransaction()
                .replace(R.id.fragment_container, ProfileFragment())
                .addToBackStack(null)
                .commit()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
