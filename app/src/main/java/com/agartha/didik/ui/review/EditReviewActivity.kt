package com.agartha.didik.ui.review

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.agartha.didik.databinding.ActivityEditReviewBinding
import com.google.android.material.chip.Chip

class EditReviewActivity : AppCompatActivity() {

    private lateinit var binding: ActivityEditReviewBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityEditReviewBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupSkills()
    }

    private fun setupSkills() {

        val skills = listOf(
            "Figma",
            "Excel",
            "Word",
            "Adobe XD"
        )

        skills.forEach { skill ->

            val chip = Chip(this)

            chip.text = skill

            binding.cgSummarySkills.addView(chip)
        }
        binding.btnPublish.setOnClickListener {

            startActivity(
                Intent(
                    this,
                    ReviewSuccessActivity::class.java
                )
            )
        }
    }
}