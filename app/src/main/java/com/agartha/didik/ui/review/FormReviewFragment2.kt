package com.agartha.didik.ui.review

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import com.agartha.didik.R
import com.agartha.didik.databinding.FragmentFormReview2Binding
import com.agartha.didik.ui.ViewModelFactory
import com.google.android.material.chip.Chip

class FormReviewFragment2 : Fragment() {

    private var _binding: FragmentFormReview2Binding? = null
    private val binding get() = _binding!!

    private val viewModel: ReviewViewModel by activityViewModels {
        ViewModelFactory.getInstance(requireContext())
    }

    private val currentSkills = mutableListOf<String>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentFormReview2Binding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Pre-fill data
        binding.etPro.setText(viewModel.tempPro)
        binding.etKontra.setText(viewModel.tempKontra)
        
        // Clear default chips and load from viewModel
        if (viewModel.tempSkills.isNotEmpty()) {
            currentSkills.clear()
            currentSkills.addAll(viewModel.tempSkills)
        }
        refreshSkillsChips()

        binding.btnAddSkill.setOnClickListener {
            showAddSkillDialog()
        }

        binding.btnNextStep.setOnClickListener {
            saveStep2()
        }

        binding.btnBack.setOnClickListener {
            parentFragmentManager.popBackStack()
        }
    }

    private fun showAddSkillDialog() {
        val builder = AlertDialog.Builder(requireContext())
        builder.setTitle("Tambah Ilmu/Skill")

        val input = EditText(requireContext())
        input.hint = "Misal: Figma, Android, SQL"
        builder.setView(input)

        builder.setPositiveButton("Tambah") { dialog, _ ->
            val skill = input.text.toString().trim()
            if (skill.isNotEmpty()) {
                if (!currentSkills.contains(skill)) {
                    currentSkills.add(skill)
                    addSkillChip(skill)
                } else {
                    Toast.makeText(requireContext(), "Skill sudah ada", Toast.LENGTH_SHORT).show()
                }
            }
            dialog.dismiss()
        }
        builder.setNegativeButton("Batal") { dialog, _ -> dialog.cancel() }

        builder.show()
    }

    private fun addSkillChip(skill: String) {
        val chip = Chip(requireContext()).apply {
            text = skill
            isCloseIconVisible = true
            setChipBackgroundColorResource(R.color.brand_orange)
            setTextColor(resources.getColor(R.color.pure_white, null))
            setCloseIconTintResource(R.color.pure_white)
            
            setOnCloseIconClickListener {
                currentSkills.remove(skill)
                binding.cgSkills.removeView(this)
            }
        }
        // Add before the "+" button
        val index = binding.cgSkills.indexOfChild(binding.btnAddSkill)
        binding.cgSkills.addView(chip, index)
    }

    private fun refreshSkillsChips() {
        // Remove all except the add button
        val count = binding.cgSkills.childCount
        for (i in count - 1 downTo 0) {
            val view = binding.cgSkills.getChildAt(i)
            if (view.id != R.id.btnAddSkill) {
                binding.cgSkills.removeViewAt(i)
            }
        }
        currentSkills.forEach { addSkillChip(it) }
    }

    private fun saveStep2() {
        val pro = binding.etPro.text.toString().trim()
        val kontra = binding.etKontra.text.toString().trim()

        if (pro.isEmpty() || kontra.isEmpty()) {
            Toast.makeText(requireContext(), "Harap isi Pro dan Kontra", Toast.LENGTH_SHORT).show()
            return
        }

        viewModel.tempPro = pro
        viewModel.tempKontra = kontra
        viewModel.tempSkills = currentSkills

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
