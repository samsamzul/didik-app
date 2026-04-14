package com.agartha.didik

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.agartha.didik.databinding.FragmentDetailReviewBinding

class DetailReviewFragment : Fragment() {

    private var _binding: FragmentDetailReviewBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentDetailReviewBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Ambil data dari bundle
        val company = arguments?.getString("company")
        val position = arguments?.getString("position")

        // Set ke UI
        binding.tvDetailCompany.text = company
        binding.tvDetailPosition.text = position
        binding.rbDetail.rating = arguments?.getFloat("rating") ?: 0f
        binding.tvDetailJobDesc.text = arguments?.getString("job")
        binding.tvDetailReview.text = arguments?.getString("review")
        binding.tvDetailReviewer.text = "Reviewed by: ${arguments?.getString("reviewer")}"

        binding.ivBack.setOnClickListener {
            parentFragmentManager.popBackStack()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
