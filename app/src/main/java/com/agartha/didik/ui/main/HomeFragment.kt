package com.agartha.didik.ui.main

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
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
    
    private val viewModel: ReviewViewModel by viewModels {
        ViewModelFactory.getInstance(requireContext())
    }

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
        
        viewModel.reviews.observe(viewLifecycleOwner) { reviews ->
            setupRecommendations(reviews.take(3))
            setupCompanies(reviews)
        }
    }

    private fun setupGreeting() {
        val userName = preferenceManager.getUserName()
        if (!userName.isNullOrEmpty()) {
            binding.tvGreeting.text = "Halo, $userName 👋"
        }
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
