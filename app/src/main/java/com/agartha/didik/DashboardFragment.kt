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

class DashboardFragment : Fragment() {

    private var _binding: FragmentDashboardBinding? = null
    private val binding get() = _binding!!

    // Inisialisasi ViewModel
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

        // 1. Setup UI Dasar (Greeting dari SharedPreferences)
        val sharedPref = requireActivity().getSharedPreferences("UserPrefs", Context.MODE_PRIVATE)
        val username = sharedPref.getString("user_name", "Nadilah")
        binding.tvGreeting.text = "Halo, $username! 👋"

        // 2. Setup RecyclerView (Layout Manager-nya dulu)
        binding.rvReview.layoutManager = LinearLayoutManager(context)

        // 3. Hubungkan ke ViewModel (Gudang Data)
        viewModel = ViewModelProvider(requireActivity())[ReviewViewModel::class.java]

        // 4. Pantau (Observe) perubahan data di ViewModel
        viewModel.reviews.observe(viewLifecycleOwner) { list ->
            if (list.isEmpty()) {
                binding.rvReview.visibility = View.GONE
                // binding.layoutEmpty.visibility = View.VISIBLE // Sesuaikan ID di XML-mu ya
            } else {
                binding.rvReview.visibility = View.VISIBLE
                // binding.layoutEmpty.visibility = View.GONE
                
                binding.rvReview.adapter = ReviewAdapter(list) { selectedReview ->
                    // Apa yang terjadi pas kartu di-klik?
                    val detailFragment = DetailReviewFragment()

                    // Kirim data pake Bundle
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
            }
        }

        // 5. Logika Navigasi (Tombol-tombol)
        binding.fabAddReview.setOnClickListener {
            parentFragmentManager.beginTransaction()
                .replace(R.id.fragment_container, FormReviewFragment())
                .addToBackStack(null)
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
