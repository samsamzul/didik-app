package com.agartha.didik.ui.review

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.agartha.didik.databinding.FragmentHistoryBinding
import com.agartha.didik.adapter.ReviewAdapter
import com.agartha.didik.ui.ViewModelFactory
import com.agartha.didik.utils.PreferenceManager

class HistoryFragment : Fragment() {

    private var _binding: FragmentHistoryBinding? = null
    private val binding get() = _binding!!
    
    private lateinit var preferenceManager: PreferenceManager
    
    private val viewModel: ReviewViewModel by viewModels {
        ViewModelFactory.getInstance(requireContext())
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHistoryBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        preferenceManager = PreferenceManager(requireContext())
        val currentUserId = preferenceManager.getUserId()

        binding.rvHistory.layoutManager = LinearLayoutManager(requireContext())
        
        viewModel.reviews.observe(viewLifecycleOwner) { reviews ->
            val userSpecificReviews = reviews.filter { it.userId == currentUserId }
            
            if (userSpecificReviews.isEmpty()) {
                binding.rvHistory.visibility = View.GONE
            } else {
                binding.rvHistory.visibility = View.VISIBLE
                binding.rvHistory.adapter = ReviewAdapter(userSpecificReviews) { review ->
                    // Kirim data via Intent karena beda Activity
                    val intent = Intent(requireContext(), AddReviewActivity::class.java).apply {
                        putExtra("EXTRA_REVIEW_ID", review.reviewId)
                        putExtra("EXTRA_COMPANY", review.companyName)
                        putExtra("EXTRA_POSITION", review.position)
                        putExtra("EXTRA_REVIEW_TEXT", review.reviewText)
                        putExtra("EXTRA_RATING", review.rating.toInt())
                        putExtra("EXTRA_WORKLOAD", review.ratingWorkload)
                        putExtra("EXTRA_MENTORSHIP", review.ratingMentorship)
                        putExtra("EXTRA_CULTURE", review.ratingCulture)
                        putExtra("EXTRA_PROS", review.pros)
                        putExtra("EXTRA_CONS", review.cons)
                    }
                    startActivity(intent)
                }
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
