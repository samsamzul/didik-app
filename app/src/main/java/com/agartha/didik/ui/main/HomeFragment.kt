package com.agartha.didik.ui.main

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.agartha.didik.R
import com.agartha.didik.databinding.FragmentHomeBinding
import com.agartha.didik.ui.adapter.ReviewAdapter
import com.agartha.didik.ui.company.CompanyDetailFragment
import com.agartha.didik.utils.PreferenceManager

class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!
    private lateinit var preferenceManager: PreferenceManager

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
        setupRecommendations()
        setupCompanies()
    }

    private fun setupGreeting() {
        val userName = preferenceManager.getUserName()
        if (!userName.isNullOrEmpty()) {
            binding.tvGreeting.text = "Halo, $userName 👋"
        }
    }

    private fun setupRecommendations() {
        binding.rvRecommendations.layoutManager = 
            LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
        
        // Using a dummy list for now to match UI
        binding.rvRecommendations.adapter = ReviewAdapter(listOf(1, 2, 3)) {
            navigateToDetail("Figma")
        }
    }

    private fun setupCompanies() {
        binding.rvCompanies.layoutManager = LinearLayoutManager(requireContext())
        binding.rvCompanies.adapter = ReviewAdapter(listOf(1, 2, 3, 4)) {
            navigateToDetail("Tokopedia")
        }
    }

    private fun navigateToDetail(companyName: String) {
        val fragment = CompanyDetailFragment().apply {
            arguments = Bundle().apply {
                putString("company", companyName)
            }
        }
        parentFragmentManager.beginTransaction()
            .replace(R.id.fragment_container, fragment)
            .addToBackStack(null)
            .commit()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
