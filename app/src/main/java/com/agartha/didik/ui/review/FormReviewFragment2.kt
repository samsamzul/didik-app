package com.agartha.didik.ui.review

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import com.agartha.didik.R
import com.agartha.didik.databinding.FragmentFormReview2Binding
import com.agartha.didik.ui.ViewModelFactory

class FormReviewFragment2 : Fragment() {

    private var _binding: FragmentFormReview2Binding? = null
    private val binding get() = _binding!!

    // Menggunakan activityViewModels yang SAMA dengan Fragment 1
    private val viewModel: ReviewViewModel by activityViewModels {
        ViewModelFactory.getInstance(requireContext())
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentFormReview2Binding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Pre-fill data jika dalam mode EDIT
        if (viewModel.editingReviewId != null) {
            binding.etPro.setText(viewModel.tempPro)
            binding.etKontra.setText(viewModel.tempKontra)
        }

        binding.btnNextStep.setOnClickListener {
            saveStep2()
        }

        binding.btnBack.setOnClickListener {
            parentFragmentManager.popBackStack()
        }
    }

    private fun saveStep2() {
        val pro = binding.etPro.text.toString().trim()
        val kontra = binding.etKontra.text.toString().trim()

        if (pro.isEmpty() || kontra.isEmpty()) {
            Toast.makeText(requireContext(), "Harap isi Pro dan Kontra", Toast.LENGTH_SHORT).show()
            return
        }

        // Simpan ke ViewModel
        viewModel.tempPro = pro
        viewModel.tempKontra = kontra
        // Skill bisa ditambahkan dari logika ChipGroup jika sudah ada
        viewModel.tempSkills = listOf("Figma", "Design System")

        // Navigasi ke Step Final (3)
        parentFragmentManager.beginTransaction()
            .replace(R.id.fragment_container, FormReviewFragmentFinal3())
            .addToBackStack(null)
            .commit()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
