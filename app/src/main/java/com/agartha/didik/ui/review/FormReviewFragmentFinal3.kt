package com.agartha.didik.ui.review

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import com.agartha.didik.R
import com.agartha.didik.data.local.entity.UlasanEntity
import com.agartha.didik.databinding.FragmentFormReviewFinal3Binding
import com.agartha.didik.ui.ViewModelFactory
import com.agartha.didik.utils.PreferenceManager
import com.google.android.material.chip.Chip

class FormReviewFragmentFinal3 : Fragment() {

    private var _binding: FragmentFormReviewFinal3Binding? = null
    private val binding get() = _binding!!
    private lateinit var preferenceManager: PreferenceManager

    private val viewModel: ReviewViewModel by activityViewModels {
        ViewModelFactory.getInstance(requireContext())
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentFormReviewFinal3Binding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        preferenceManager = PreferenceManager(requireContext())
        
        setupReviewSummary()

        // Sync anonymous toggle with viewModel state
        binding.switchAnonim.isChecked = viewModel.tempIsAnonim

        if (viewModel.editingReviewId != null) {
            binding.btnPublish.text = "Update Review"
        }

        binding.btnPublish.setOnClickListener {
            publishFinalReview()
        }

        binding.btnBack.setOnClickListener {
            parentFragmentManager.popBackStack()
        }
    }

    private fun setupReviewSummary() {
        with(binding) {
            tvSummaryPosition.text = viewModel.tempPosition
            tvSummaryCompany.text = "${viewModel.tempCompanyName} - Internship"
            
            rbCulture.rating = viewModel.tempRatingCulture.toFloat()
            rbWorkload.rating = viewModel.tempRatingWorkload.toFloat()
            rbMentorship.rating = viewModel.tempRatingMentorship.toFloat()

            tvSummaryPro.text = formatToBulletList(viewModel.tempPro)
            tvSummaryKontra.text = formatToBulletList(viewModel.tempKontra)

            cgSummarySkills.removeAllViews()
            viewModel.tempSkills.forEach { skill ->
                val chip = Chip(requireContext()).apply {
                    text = skill
                    isCheckable = false
                    isClickable = false
                    setChipBackgroundColorResource(R.color.soft_pink)
                    setTextColor(resources.getColor(R.color.brand_orange, null))
                    chipStrokeWidth = 0f
                }
                cgSummarySkills.addView(chip)
            }
        }
    }

    private fun formatToBulletList(text: String): String {
        if (text.isEmpty()) return "-"
        return text.split("\n")
            .filter { it.isNotBlank() }
            .joinToString("\n") { "• $it" }
    }

    private fun publishFinalReview() {
        val userId = preferenceManager.getUserId()
        if (userId == -1) {
            Toast.makeText(requireContext(), "Sesi berakhir, silakan login kembali", Toast.LENGTH_SHORT).show()
            return
        }

        val isAnonim = binding.switchAnonim.isChecked

        val reviewData = UlasanEntity(
            id = viewModel.editingReviewId ?: 0,
            userId = userId,
            perusahaanId = if (viewModel.tempPerusahaanId != 0) viewModel.tempPerusahaanId else 1,
            namaPosisi = viewModel.tempPosition,
            deskripsiKerja = viewModel.tempReviewText,
            ratingWorkload = viewModel.tempRatingWorkload,
            ratingMentorship = viewModel.tempRatingMentorship,
            ratingCulture = viewModel.tempRatingCulture,
            teksUlasan = viewModel.tempReviewText,
            ilmuDidapat = viewModel.tempSkills.joinToString(", "),
            poinKelebihan = viewModel.tempPro,
            poinKekurangan = viewModel.tempKontra,
            isAnonim = isAnonim
        )

        if (viewModel.editingReviewId != null) {
            viewModel.updateReview(reviewData)
            Toast.makeText(requireContext(), "Ulasan berhasil diperbarui!", Toast.LENGTH_SHORT).show()
        } else {
            viewModel.insertReview(reviewData)
            Toast.makeText(requireContext(), "Ulasan berhasil dikirim!", Toast.LENGTH_SHORT).show()
        }
        
        activity?.finish()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
