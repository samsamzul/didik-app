package com.agartha.didik.ui.review

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import com.agartha.didik.R
import com.agartha.didik.databinding.FragmentFormReviewBinding
import com.agartha.didik.ui.ViewModelFactory

class FormReviewFragment : Fragment() {

    private var _binding: FragmentFormReviewBinding? = null
    private val binding get() = _binding!!

    // Menggunakan activityViewModels agar data tersimpan selama siklus hidup Activity (Shared ViewModel)
    private val viewModel: ReviewViewModel by activityViewModels {
        ViewModelFactory.getInstance(requireContext())
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentFormReviewBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.btnSave.setOnClickListener {
            saveToViewModel()
        }
    }

    private fun saveToViewModel() {
        val companyName = binding.etCompanyName.text.toString().trim()
        val position = binding.etPosition.text.toString().trim()
        val reviewText = binding.etReviewText.text.toString().trim()
        
        val ratingUtama = binding.rbReview.rating.toInt()
        val ratingWork = binding.rbWorkload.rating.toInt()
        val ratingMent = binding.rbMentorship.rating.toInt()
        val ratingCulture = binding.rbCulture.rating.toInt()

        if (companyName.isEmpty() || position.isEmpty() || reviewText.isEmpty()) {
            Toast.makeText(requireContext(), "Harap isi semua bidang", Toast.LENGTH_SHORT).show()
            return
        }

        // Simpan data ke Shared ViewModel agar bisa diakses di Fragment selanjutnya
        viewModel.tempCompanyName = companyName
        viewModel.tempPosition = position
        viewModel.tempReviewText = reviewText
        viewModel.tempRating = ratingUtama
        viewModel.tempRatingWorkload = ratingWork
        viewModel.tempRatingMentorship = ratingMent
        viewModel.tempRatingCulture = ratingCulture

        // Navigasi ke Step 2
        parentFragmentManager.beginTransaction()
            .replace(R.id.fragment_container, FormReviewFragment2())
            .addToBackStack(null)
            .commit()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
