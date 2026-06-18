package com.agartha.didik.ui.review

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.agartha.didik.R
import com.agartha.didik.databinding.ActivityAddReviewBinding
import com.agartha.didik.ui.ViewModelFactory

class AddReviewActivity : AppCompatActivity() {

    private lateinit var binding: ActivityAddReviewBinding
    
    // Inisialisasi ViewModel di level Activity agar datanya bisa di-share ke Fragment (activityViewModels)
    private val viewModel: ReviewViewModel by viewModels {
        ViewModelFactory.getInstance(this)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityAddReviewBinding.inflate(layoutInflater)
        setContentView(binding.root)

        ViewCompat.setOnApplyWindowInsetsListener(binding.main) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        // Tangkap data dari HistoryFragment jika dalam mode EDIT
        val reviewId = intent.getIntExtra("EXTRA_REVIEW_ID", -1)
        if (reviewId != -1) {
            viewModel.setReviewToEdit(
                id = reviewId,
                company = intent.getStringExtra("EXTRA_COMPANY") ?: "",
                pos = intent.getStringExtra("EXTRA_POSITION") ?: "",
                text = intent.getStringExtra("EXTRA_REVIEW_TEXT") ?: "",
                rating = intent.getIntExtra("EXTRA_RATING", 0),
                work = intent.getIntExtra("EXTRA_WORKLOAD", 0),
                ment = intent.getIntExtra("EXTRA_MENTORSHIP", 0),
                cult = intent.getIntExtra("EXTRA_CULTURE", 0),
                pros = intent.getStringExtra("EXTRA_PROS") ?: "",
                cons = intent.getStringExtra("EXTRA_CONS") ?: ""
            )
        } else {
            // Jika bukan mode edit, pastikan state form bersih
            viewModel.clearFormState()
        }

        if (savedInstanceState == null) {
            supportFragmentManager.beginTransaction()
                .replace(R.id.fragment_container, FormReviewFragment())
                .commit()
        }
    }
}
