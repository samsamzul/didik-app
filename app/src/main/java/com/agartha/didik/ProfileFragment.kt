package com.agartha.didik

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.agartha.didik.adapter.ReviewAdapter
import com.agartha.didik.data.ReviewViewModel
import com.agartha.didik.databinding.FragmentProfileBinding

/**
 * Fragment untuk menampilkan profil user dan riwayat review yang pernah dibuat oleh user tersebut.
 */
class ProfileFragment : Fragment() {

    private var _binding: FragmentProfileBinding? = null
    private val binding get() = _binding!!
    private lateinit var viewModel: ReviewViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentProfileBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // 1. Mengambil nama user dari SharedPreferences (data sesi login)
        val sharedPref = requireActivity().getSharedPreferences("UserPrefs", Context.MODE_PRIVATE)
        val myName = sharedPref.getString("user_name", "Nadilah Nusa") ?: "Nadilah Nusa"

        // Tampilkan nama di UI profil
        binding.tvProfileName.text = myName

        // 2. Inisialisasi ViewModel untuk mengambil data review
        viewModel = ViewModelProvider(requireActivity())[ReviewViewModel::class.java]

        // 3. Mengatur layout daftar riwayat (RecyclerView)
        binding.rvMyHistory.layoutManager = LinearLayoutManager(context)

        // 4. Observasi data review: Filter hanya review milik user yang sedang login
        viewModel.reviews.observe(viewLifecycleOwner) { allReviews ->
            val myHistory = allReviews.filter { it.reviewerName == myName }

            if (myHistory.isNotEmpty()) {
                binding.rvMyHistory.visibility = View.VISIBLE
                binding.layoutEmptyProfile.visibility = View.GONE
                
                // Menampilkan riwayat review ke dalam adapter
                binding.rvMyHistory.adapter = ReviewAdapter(myHistory) { selectedReview ->
                    // Jika salah satu review diklik, tampilkan detailnya
                    val detailFragment = DetailReviewFragment()

                    val bundle = Bundle()
                    bundle.putString("company", selectedReview.companyName)
                    bundle.putString("position", selectedReview.position)
                    bundle.putString("job", selectedReview.jobDesc)
                    bundle.putString("review", selectedReview.reviewText)
                    bundle.putFloat("rating", selectedReview.rating)
                    bundle.putString("reviewer", selectedReview.reviewerName)
                    detailFragment.arguments = bundle

                    parentFragmentManager.beginTransaction()
                        .replace(R.id.fragment_container, detailFragment)
                        .addToBackStack(null)
                        .commit()
                }
            } else {
                // Tampilan jika user belum pernah menulis review
                binding.rvMyHistory.visibility = View.GONE
                binding.layoutEmptyProfile.visibility = View.VISIBLE
            }
        }

        // 5. Logika Tombol Keluar (Logout)
        binding.btnLogout.setOnClickListener {
            // Menghapus sesi login di SharedPreferences
            val editor = sharedPref.edit()
            editor.remove("user_name") 
            editor.apply()

            // Kembali ke halaman Login dan hapus riwayat navigasi agar tidak bisa di-back ke profil
            parentFragmentManager.beginTransaction()
                .replace(R.id.fragment_container, LoginFragment())
                .apply {
                    for (i in 0 until parentFragmentManager.backStackEntryCount) {
                        parentFragmentManager.popBackStack()
                    }
                }
                .commit()

            Toast.makeText(requireContext(), "Berhasil keluar. Sampai jumpa!", Toast.LENGTH_SHORT).show()
        }

        // 6. Logika Tombol Kembali (Back)
        binding.ivBack.setOnClickListener {
            parentFragmentManager.popBackStack()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
