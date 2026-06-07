package com.agartha.didik.ui.company

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.agartha.didik.R
import com.agartha.didik.databinding.FragmentCompanyDetailBinding
import com.agartha.didik.ui.adapter.ReviewAdapter
import com.agartha.didik.ui.review.FormReviewFragment

class CompanyDetailFragment : Fragment() {

    private var _binding: FragmentCompanyDetailBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentCompanyDetailBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val companyName = arguments?.getString("company") ?: "Unknown Company"
        binding.tvToolbarTitle.text = companyName
        
        binding.rvStudentReviews.layoutManager = LinearLayoutManager(requireContext())
        // Set dummy adapter or from ViewModel
        binding.rvStudentReviews.adapter = ReviewAdapter(emptyList()) {}

        binding.ivBackDetail.setOnClickListener {
            parentFragmentManager.popBackStack()
        }

        binding.btnWriteReview.setOnClickListener {
            parentFragmentManager.beginTransaction()
                .replace(R.id.fragment_container, FormReviewFragment())
                .addToBackStack(null)
                .commit()
        }

        binding.btnApply.setOnClickListener {
            Toast.makeText(requireContext(), "Application submitted!", Toast.LENGTH_SHORT).show()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
