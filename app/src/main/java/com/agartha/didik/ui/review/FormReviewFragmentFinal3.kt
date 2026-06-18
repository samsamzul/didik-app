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
import com.google.android.material.chip.Chip

class FormReviewFragmentFinal3 : Fragment() {

    private var _binding: FragmentFormReviewFinal3Binding? = null
    private val binding get() = _binding!!

    // Shared ViewModel antar Fragment dalam satu Activity
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

        // Tampilkan rangkuman ulasan dari data di ViewModel
        setupReviewSummary()

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
            tvSummaryCompany.text = "${viewModel.tempCompanyName} - Summer 2024"
            
            // Set ratings yang dikumpulkan dari Step 1
            rbCulture.rating = viewModel.tempRatingCulture.toFloat()
            rbWorkload.rating = viewModel.tempRatingWorkload.toFloat()
            rbMentorship.rating = viewModel.tempRatingMentorship.toFloat()

            // Set Pro & Kontra dari Step 2 (ditambah bullet point agar rapi)
            tvSummaryPro.text = formatToBulletList(viewModel.tempPro)
            tvSummaryKontra.text = formatToBulletList(viewModel.tempKontra)

            // Tampilkan Skills sebagai Chips
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
        // Buat entity untuk disimpan ke database
        val newReview = UlasanEntity(
            userId = 1, // Placeholder: seharusnya ID user yang sedang login
            perusahaanId = 1, // Placeholder: seharusnya ID perusahaan yang dipilih
            namaPosisi = viewModel.tempPosition,
            deskripsiKerja = viewModel.tempReviewText,
            ratingWorkload = viewModel.tempRatingWorkload,
            ratingMentorship = viewModel.tempRatingMentorship,
            ratingCulture = viewModel.tempRatingCulture,
            teksUlasan = viewModel.tempReviewText,
            ilmuDidapat = viewModel.tempSkills.joinToString(", "),
            poinKelebihan = viewModel.tempPro,
            poinKekurangan = viewModel.tempKontra,
            isAnonim = binding.switchAnonim.isChecked
        )

        // Simpan via ViewModel
        viewModel.insertReview(newReview)
        
        Toast.makeText(requireContext(), "Ulasan berhasil dikirim!", Toast.LENGTH_SHORT).show()
        
        // Kembali ke Home atau tutup activity
        activity?.finish()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
