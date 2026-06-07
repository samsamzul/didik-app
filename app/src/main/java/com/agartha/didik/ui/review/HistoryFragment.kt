package com.agartha.didik.ui.review

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.agartha.didik.R
import com.agartha.didik.adapter.ReviewAdapter
import com.agartha.didik.databinding.FragmentHistoryBinding

class HistoryFragment : Fragment() {

    private var _binding: FragmentHistoryBinding? = null
    private val binding get() = _binding!!
    private lateinit var viewModel: ReviewViewModel
    private var currentCategory = "All"

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHistoryBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val sharedPref = requireActivity().getSharedPreferences("UserPrefs", Context.MODE_PRIVATE)
        val myName = sharedPref.getString("user_name", "") ?: ""

        viewModel = ViewModelProvider(requireActivity())[ReviewViewModel::class.java]

        binding.rvHistory.layoutManager = LinearLayoutManager(context)

        binding.chipGroupFilter.setOnCheckedStateChangeListener { _, checkedIds ->
            currentCategory = when (checkedIds.firstOrNull()) {
                R.id.chipTech -> "Tech"
                R.id.chipDesign -> "Design"
                R.id.chipBusiness -> "Business"
                else -> "All"
            }
            updateList(myName)
        }

        viewModel.reviews.observe(viewLifecycleOwner) {
            updateList(myName)
        }
    }

    private fun updateList(myName: String) {
        val allReviews = viewModel.reviews.value ?: emptyList()
        val myHistory = allReviews.filter { it.reviewerName.toString() == myName }

        val filtered = if (currentCategory == "All") {
            myHistory
        } else {
            myHistory.filter { it.category.toString() == currentCategory }
        }

        if (filtered.isNotEmpty()) {
            binding.rvHistory.visibility = View.VISIBLE
            binding.layoutEmptyHistory.visibility = View.GONE

            binding.rvHistory.adapter = ReviewAdapter(filtered) { selectedReview ->
                // 🌟 NAVIGASI SAKTI PAKAI INTENT & TO_STRING() AMAN 🌟
                // Kita arahkan ke DetailReviewActivity (ubah DetailReviewFragment jadi Activity ya nanti, Nad!)
                val intent = Intent(requireContext(), DetailReviewActivity::class.java).apply {
                    putExtra("company", selectedReview.companyName.toString())
                    putExtra("position", selectedReview.position.toString())
                    putExtra("job", selectedReview.jobDesc.toString())
                    putExtra("review", selectedReview.reviewText.toString())
                    putExtra("rating", selectedReview.rating) // Float aman tanpa toString
                    putExtra("reviewer", selectedReview.reviewerName.toString())
                }
                startActivity(intent)
            }
        } else {
            binding.rvHistory.visibility = View.GONE
            binding.layoutEmptyHistory.visibility = View.VISIBLE
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}