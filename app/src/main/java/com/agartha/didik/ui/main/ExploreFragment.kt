package com.agartha.didik.ui.main

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.agartha.didik.R
import com.agartha.didik.databinding.FragmentExploreBinding
import com.agartha.didik.ui.adapter.ReviewAdapter
import com.agartha.didik.ui.company.CompanyDetailFragment

class ExploreFragment : Fragment() {

    private var _binding: FragmentExploreBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentExploreBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.rvInternships.layoutManager = LinearLayoutManager(requireContext())
        binding.rvInternships.adapter = ReviewAdapter(listOf(1, 2, 3, 4, 5)) {
            val fragment = CompanyDetailFragment().apply {
                arguments = Bundle().apply {
                    putString("company", "Strip")
                }
            }
            parentFragmentManager.beginTransaction()
                .replace(R.id.fragment_container, fragment)
                .addToBackStack(null)
                .commit()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
