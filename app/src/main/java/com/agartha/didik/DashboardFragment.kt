package com.agartha.didik

import android.annotation.SuppressLint
import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.agartha.didik.data.ReviewModel
import com.agartha.didik.adapter.ReviewAdapter
import com.agartha.didik.data.ReviewViewModel
import com.agartha.didik.databinding.FragmentDashboardBinding

/**
 * Fragment utama yang menampilkan daftar review dari semua user (Beranda/Dashboard).
 */
class DashboardFragment : Fragment() {

    // View Binding untuk mengakses komponen UI tanpa findViewById
    private var _binding: FragmentDashboardBinding? = null
    private val binding get() = _binding!!

    // ViewModel untuk mengelola data review secara reaktif
    private lateinit var viewModel: ReviewViewModel

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

        // 1. Inisialisasi SharedPreferences untuk mengambil nama user yang login
        val sharedPref = requireActivity().getSharedPreferences("UserPrefs", Context.MODE_PRIVATE)
        val namaUser = sharedPref.getString("user_name", "User")
        binding.tvGreeting.text = "Halo, $namaUser! 👋"

        // 2. Konfigurasi RecyclerView dengan LayoutManager (Linear/Daftar menurun)
        binding.rvReview.layoutManager = LinearLayoutManager(context)

        // 3. Menghubungkan Fragment dengan ViewModel
        viewModel = ViewModelProvider(requireActivity())[ReviewViewModel::class.java]

        // 4. Observasi data dari ViewModel. Jika data review berubah, UI akan otomatis update.
        viewModel.reviews.observe(viewLifecycleOwner) { list ->
            if (list.isEmpty()) {
                binding.rvReview.visibility = View.GONE
            } else {
                binding.rvReview.visibility = View.VISIBLE
                
                // Set Adapter untuk menampilkan data ke RecyclerView
                binding.rvReview.adapter = ReviewAdapter(list) { selectedReview ->
                    // Logika ketika salah satu item review diklik
                    val detailFragment = DetailReviewFragment()

                    // Membungkus data review ke dalam Bundle untuk dikirim ke DetailFragment
                    val bundle = Bundle()
                    bundle.putString("company", selectedReview.companyName)
                    bundle.putString("position", selectedReview.position)
                    bundle.putString("job", selectedReview.jobDesc)
                    bundle.putString("review", selectedReview.reviewText)
                    bundle.putFloat("rating", selectedReview.rating)
                    bundle.putString("reviewer", selectedReview.reviewerName)
                    detailFragment.arguments = bundle

                    // Berpindah ke Fragment Detail
                    parentFragmentManager.beginTransaction()
                        .replace(R.id.fragment_container, detailFragment)
                        .addToBackStack(null)
                        .commit()
                }
            }
        }

        // 5. Navigasi ke halaman Tambah Review (FAB)
        binding.fabAddReview.setOnClickListener {
            parentFragmentManager.beginTransaction()
                .replace(R.id.fragment_container, FormReviewFragment())
                .addToBackStack(null)
                .commit()
        }

        // 6. Navigasi ke halaman Profil User
        binding.ivProfile.setOnClickListener {
            parentFragmentManager.beginTransaction()
                .replace(R.id.fragment_container, ProfileFragment())
                .addToBackStack(null)
                .commit()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        // Membersihkan binding untuk menghindari memory leak
        _binding = null
    }
}
