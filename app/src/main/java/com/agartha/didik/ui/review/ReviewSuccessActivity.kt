package com.agartha.didik.ui.review

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.agartha.didik.databinding.ActivityReviewSuccessBinding
import com.agartha.didik.ui.main.MainActivity

class ReviewSuccessActivity : AppCompatActivity() {

    private lateinit var binding: ActivityReviewSuccessBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityReviewSuccessBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupAction()
    }

    private fun setupAction() {

        binding.btnBackHome.setOnClickListener {

            startActivity(
                Intent(
                    this,
                    MainActivity::class.java
                )
            )

            finishAffinity()
        }

        binding.btnViewReview.setOnClickListener {

            finish()
        }
    }
}