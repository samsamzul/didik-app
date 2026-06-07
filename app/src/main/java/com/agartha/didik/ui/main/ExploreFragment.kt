package com.agartha.didik.ui.main

import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.agartha.didik.R
import com.agartha.didik.adapter.ReviewAdapter
import com.agartha.didik.databinding.FragmentExploreBinding // 🌟 KUNCI: Pakai ExploreBinding sesuai nama XML-mu!
import com.agartha.didik.ui.company.CompanyDetailActivity
import com.agartha.didik.ui.review.ReviewViewModel // 🌟 KUNCI: Import jalan rumah ViewModel-mu!

class ExploreFragment : Fragment() {

    private var _binding: FragmentExploreBinding? = null // 🌟 Ganti ke FragmentExploreBinding
    private val binding get() = _binding!!
    private lateinit var viewModel: ReviewViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // 🌟 KUNCI: Inflate-nya juga diarahkan ke FragmentExploreBinding
        _binding = FragmentExploreBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Inisialisasi ViewModel secara aman pakai .get()
        viewModel = ViewModelProvider(requireActivity()).get(ReviewViewModel::class.java)

        binding.rvSearchResults.layoutManager = LinearLayoutManager(context)

        binding.etSearch.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                filterReviews(s.toString())
            }
            override fun afterTextChanged(s: Editable?) {}
        })

        filterReviews("")
    }

    private fun filterReviews(query: String) {
        viewModel.reviews.observe(viewLifecycleOwner) { allReviews ->
            val filtered = if (query.isEmpty()) {
                allReviews
            } else {
                allReviews.filter {
                    it.companyName.toString().contains(query, ignoreCase = true) ||
                            it.position.toString().contains(query, ignoreCase = true)
                }
            }

            if (filtered.isEmpty()) {
                binding.rvSearchResults.visibility = View.GONE
                binding.layoutEmptySearch.visibility = View.VISIBLE
            } else {
                binding.rvSearchResults.visibility = View.VISIBLE
                binding.layoutEmptySearch.visibility = View.GONE

                val uniqueCompanies = filtered.distinctBy { it.companyName }

                binding.rvSearchResults.adapter = ReviewAdapter(uniqueCompanies) { selected ->
                    val intent = Intent(requireContext(), CompanyDetailActivity::class.java).apply {
                        putExtra("company", selected.companyName.toString())
                        putExtra("position", selected.position.toString())
                        putExtra("category", selected.category.toString())
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