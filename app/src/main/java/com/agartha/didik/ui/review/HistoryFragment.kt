package com.agartha.didik.ui.review

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.widget.doOnTextChanged
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.agartha.didik.R
import com.agartha.didik.databinding.FragmentHistoryBinding
import com.agartha.didik.adapter.ReviewAdapter
import com.agartha.didik.ui.ViewModelFactory
import com.agartha.didik.ui.review.ReviewModel
import com.agartha.didik.utils.PreferenceManager

class HistoryFragment : Fragment() {

    private var _binding: FragmentHistoryBinding? = null
    private val binding get() = _binding!!
    
    private lateinit var preferenceManager: PreferenceManager
    
    private val viewModel: ReviewViewModel by activityViewModels {
        ViewModelFactory.getInstance(requireContext())
    }

    private var allUserReviews: List<ReviewModel> = listOf()

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
        
        setupSearchAndFilters()

        viewModel.reviews.observe(viewLifecycleOwner) { reviews ->
            allUserReviews = reviews.filter { it.userId == currentUserId }
            filterData(binding.etSearchHistory.text.toString())
        }
    }

    private fun setupSearchAndFilters() {
        binding.etSearchHistory.doOnTextChanged { text, _, _, _ ->
            filterData(text.toString())
        }

        binding.cgHistoryFilters.setOnCheckedStateChangeListener { _, _ ->
            filterData(binding.etSearchHistory.text.toString())
        }
    }

    private fun filterData(query: String) {
        val checkedChipId = binding.cgHistoryFilters.checkedChipId

        val filteredList = allUserReviews.filter { review ->
            val matchesSearch = review.companyName.contains(query, ignoreCase = true) ||
                    review.position.contains(query, ignoreCase = true)

            val matchesFilter = when (checkedChipId) {
                R.id.chipRemote -> {
                    review.location.contains("remote", ignoreCase = true) ||
                            review.position.contains("remote", ignoreCase = true)
                }
                R.id.chipDuration -> {
                    review.position.contains("month", ignoreCase = true) ||
                            review.position.contains("bulan", ignoreCase = true) ||
                            review.jobDesc.contains("month", ignoreCase = true) ||
                            review.jobDesc.contains("bulan", ignoreCase = true)
                }
                R.id.chipRole -> {
                    val roles = listOf("engineer", "designer", "developer", "analyst", "ui", "ux")
                    roles.any { role -> review.position.contains(role, ignoreCase = true) }
                }
                else -> true
            }

            matchesSearch && matchesFilter
        }
        updateUI(filteredList)
    }

    private fun updateUI(reviews: List<ReviewModel>) {
        if (reviews.isEmpty()) {
            binding.rvHistory.visibility = View.GONE
        } else {
            binding.rvHistory.visibility = View.VISIBLE
            binding.rvHistory.adapter = ReviewAdapter(
                listReview = reviews,
                onDeleteClick = { review ->
                    val ulasanEntity = com.agartha.didik.data.local.entity.UlasanEntity(
                        id = review.reviewId,
                        userId = review.userId,
                        perusahaanId = review.companyId,
                        namaPosisi = review.position,
                        deskripsiKerja = review.jobDesc,
                        ratingWorkload = review.ratingWorkload,
                        ratingMentorship = review.ratingMentorship,
                        ratingCulture = review.ratingCulture,
                        teksUlasan = review.reviewText,
                        ilmuDidapat = "",
                        poinKelebihan = review.pros,
                        poinKekurangan = review.cons,
                        isAnonim = review.isAnonim
                    )
                    viewModel.deleteReview(ulasanEntity)
                },
                onItemClick = { review ->
                    val intent = Intent(requireContext(), AddReviewActivity::class.java).apply {
                        putExtra("EXTRA_REVIEW_ID", review.reviewId)
                        putExtra("company_id", review.companyId)
                        putExtra("EXTRA_COMPANY", review.companyName)
                        putExtra("EXTRA_POSITION", review.position)
                        putExtra("EXTRA_REVIEW_TEXT", review.reviewText)
                        putExtra("EXTRA_RATING", review.rating.toInt())
                        putExtra("EXTRA_WORKLOAD", review.ratingWorkload)
                        putExtra("EXTRA_MENTORSHIP", review.ratingMentorship)
                        putExtra("EXTRA_CULTURE", review.ratingCulture)
                        putExtra("EXTRA_PROS", review.pros)
                        putExtra("EXTRA_CONS", review.cons)
                        putExtra("EXTRA_IS_ANONIM", review.isAnonim)
                    }
                    startActivity(intent)
                }
            )
        }
    }

    override fun onResume() {
        super.onResume()
        // Pastikan data terbaru diambil setiap kali fragment tampil
        viewModel.loadReviewsFromDatabase()
        
        // Update current user ID just in case it changed (though unlikely here)
        val currentUserId = preferenceManager.getUserId()
        viewModel.reviews.value?.let { reviews ->
            allUserReviews = reviews.filter { it.userId == currentUserId }
            filterData(binding.etSearchHistory.text.toString())
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
