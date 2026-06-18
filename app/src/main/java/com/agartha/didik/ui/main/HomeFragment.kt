package com.agartha.didik.ui.main

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
import com.agartha.didik.databinding.FragmentHomeBinding
import com.agartha.didik.adapter.ReviewAdapter
import com.agartha.didik.ui.ViewModelFactory
import com.agartha.didik.ui.company.CompanyDetailActivity
import com.agartha.didik.ui.review.ReviewModel
import com.agartha.didik.ui.review.ReviewViewModel
import com.agartha.didik.utils.PreferenceManager

class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!
    private lateinit var preferenceManager: PreferenceManager
    
    private val viewModel: ReviewViewModel by activityViewModels {
        ViewModelFactory.getInstance(requireContext())
    }

    private var allReviews: List<ReviewModel> = listOf()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        preferenceManager = PreferenceManager(requireContext())
        setupGreeting()
        setupSearchAndFilters()
        
        viewModel.reviews.observe(viewLifecycleOwner) { reviews ->
            allReviews = reviews
            setupRecommendations(reviews.take(3))
            filterData(binding.etSearchHome.text.toString())
        }
    }

    private fun setupGreeting() {
        val userName = preferenceManager.getUserName()
        if (!userName.isNullOrEmpty()) {
            binding.tvGreeting.text = "Halo, $userName 👋"
        }
    }

    private fun setupSearchAndFilters() {
        // Logika Pencarian Real-time di halaman Home
        binding.etSearchHome.doOnTextChanged { text, _, _, _ ->
            filterData(text.toString())
        }

        // Logika Filter Chip (Fintech / Healthtech)
        binding.cgCategories.setOnCheckedStateChangeListener { _, _ ->
            val searchText = binding.etSearchHome.text.toString()
            filterData(searchText)
        }
    }

    private fun filterData(query: String) {
        val checkedChipId = binding.cgCategories.checkedChipId

        val filteredList = allReviews.filter { review ->
            // 1. Filter Search
            val matchesSearch = review.companyName.contains(query, ignoreCase = true) ||
                    review.position.contains(query, ignoreCase = true)

            // 2. Filter Kategori (Fintech & Healthtech)
            val matchesCategory = when (checkedChipId) {
                R.id.chipFintech -> {
                    review.category.contains("Finance", ignoreCase = true) ||
                            review.category.contains("Tech", ignoreCase = true) ||
                            review.companyName.contains("Bank", ignoreCase = true) ||
                            review.companyName.contains("Gojek", ignoreCase = true)
                }
                R.id.chipHealthtech -> {
                    review.category.contains("Healthcare", ignoreCase = true) ||
                            review.companyName.contains("Farma", ignoreCase = true)
                }
                else -> true // Tampilkan semua jika All dipilih
            }

            matchesSearch && matchesCategory
        }

        setupCompanies(filteredList)
    }

    private fun setupRecommendations(recommendations: List<ReviewModel>) {
        binding.rvRecommendations.layoutManager = 
            LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
        
        binding.rvRecommendations.adapter = ReviewAdapter(recommendations) { review ->
            navigateToDetail(review)
        }
    }

    private fun setupCompanies(companies: List<ReviewModel>) {
        binding.rvCompanies.layoutManager = LinearLayoutManager(requireContext())
        binding.rvCompanies.adapter = ReviewAdapter(companies) { review ->
            navigateToDetail(review)
        }
    }

    private fun navigateToDetail(review: ReviewModel) {
        val intent = Intent(requireContext(), CompanyDetailActivity::class.java).apply {
            putExtra("company", review.companyName)
            putExtra("position", review.position)
            putExtra("category", review.category)
        }
        startActivity(intent)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
