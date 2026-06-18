package com.agartha.didik.ui.main

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.widget.doOnTextChanged
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.agartha.didik.R
import com.agartha.didik.databinding.FragmentExploreBinding
import com.agartha.didik.adapter.ReviewAdapter
import com.agartha.didik.ui.ViewModelFactory
import com.agartha.didik.ui.company.CompanyDetailActivity
import com.agartha.didik.ui.review.ReviewViewModel
import com.agartha.didik.ui.review.ReviewModel

class ExploreFragment : Fragment() {

    private var _binding: FragmentExploreBinding? = null
    private val binding get() = _binding!!

    private val viewModel: ReviewViewModel by viewModels {
        ViewModelFactory.getInstance(requireContext())
    }

    private var allReviews: List<ReviewModel> = listOf()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentExploreBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupRecyclerView()
        observeViewModel()
        setupSearchAndFilters()
    }

    private fun setupRecyclerView() {
        binding.rvInternships.layoutManager = LinearLayoutManager(requireContext())
    }

    private fun observeViewModel() {
        viewModel.reviews.observe(viewLifecycleOwner) { reviews ->
            allReviews = reviews
            filterData(binding.etSearchExplore.text.toString())
        }
    }

    private fun setupSearchAndFilters() {
        // Logika Pencarian
        binding.etSearchExplore.doOnTextChanged { text, _, _, _ ->
            filterData(text.toString())
        }

        // Logika Filter Chip
        binding.cgExploreFilters.setOnCheckedStateChangeListener { _, _ ->
            val filterText = binding.etSearchExplore.text.toString()
            filterData(filterText)
        }
    }

    private fun filterData(query: String) {
        val checkedChipId = binding.cgExploreFilters.checkedChipId

        val filteredList = allReviews.filter { review ->
            // 1. Logika Pencarian (Search)
            val matchesSearch = review.companyName.contains(query, ignoreCase = true) ||
                    review.position.contains(query, ignoreCase = true)

            // 2. Logika Filter Berdasarkan Chip
            val matchesFilter = when (checkedChipId) {
                R.id.chipRemote -> {
                    // Cek kata "remote" di lokasi atau judul posisi
                    review.location.contains("remote", ignoreCase = true) ||
                            review.position.contains("remote", ignoreCase = true)
                }
                R.id.chipDuration -> {
                    // Estimasi durasi berdasarkan kata kunci di deskripsi atau posisi
                    review.position.contains("month", ignoreCase = true) ||
                            review.position.contains("bulan", ignoreCase = true) ||
                            review.jobDesc.contains("month", ignoreCase = true) ||
                            review.jobDesc.contains("bulan", ignoreCase = true)
                }
                R.id.chipRole -> {
                    // Filter peran spesifik teknologi/desain
                    val roles = listOf("engineer", "designer", "developer", "analyst", "ui", "ux")
                    roles.any { role -> review.position.contains(role, ignoreCase = true) }
                }
                else -> true // chipAll atau tidak ada yang terpilih, tampilkan semua
            }

            matchesSearch && matchesFilter
        }
        updateList(filteredList)
    }

    private fun updateList(list: List<ReviewModel>) {
        binding.rvInternships.adapter = ReviewAdapter(list) { review ->
            val intent = Intent(requireContext(), CompanyDetailActivity::class.java).apply {
                putExtra("company", review.companyName)
                putExtra("position", review.position)
                putExtra("category", review.category)
            }
            startActivity(intent)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
