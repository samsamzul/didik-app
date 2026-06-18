package com.agartha.didik.ui.company

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.agartha.didik.adapter.ReviewAdapter
import com.agartha.didik.databinding.ActivityCompanyDetailBinding // 🌟 SINKRON 100% SAMA PETA LAYOUT KAMU
import com.agartha.didik.ui.ViewModelFactory
import com.agartha.didik.ui.review.ReviewModel // 🌟 Ambil model yang kita buat menumpang di ViewModel tadi
import com.agartha.didik.ui.review.ReviewViewModel

class CompanyDetailActivity : AppCompatActivity() {

    private lateinit var binding: ActivityCompanyDetailBinding
    private val viewModel: ReviewViewModel by viewModels {
        ViewModelFactory.getInstance(this)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Inisialisasi View Binding asli Activity murni sesuai peta XML
        binding = ActivityCompanyDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Ambil data Intent kiriman dari ExploreFragment
        val companyName = intent.getStringExtra("company") ?: "Unknown Company"
        val position = intent.getStringExtra("position") ?: "Internship"
        val category = intent.getStringExtra("category") ?: "Design & Tech"

        // Pasang data ke komponen UI
        binding.tvCompanyPosition.text = position
        binding.tvCompanyCategory.text = category

        // Set up RecyclerView ulasan siswa
        binding.rvStudentReviews.layoutManager = LinearLayoutManager(this)

        // Ambil data ulasan dari ViewModel
        viewModel.reviews.observe(this) { reviews ->
            val companyReviews = reviews.filter { it.companyName == companyName }
            
            if (companyReviews.isNotEmpty()) {
                val firstReview = companyReviews[0]
                binding.tvCompanyLocation.text = firstReview.location
                binding.tvRatingBadge.text = String.format("%.1f", firstReview.rating)
                binding.tvReviewCount.text = "(${companyReviews.size} reviews)"
                
                // Set ratings progress
                binding.layoutCulture.pbRating.progress = (firstReview.ratingCulture * 20)
                binding.layoutCulture.tvRatingValue.text = "${firstReview.ratingCulture}.0/5.0"
                
                binding.layoutWorkload.pbRating.progress = (firstReview.ratingWorkload * 20)
                binding.layoutWorkload.tvRatingValue.text = "${firstReview.ratingWorkload}.0/5.0"
                
                binding.layoutMentorship.pbRating.progress = (firstReview.ratingMentorship * 20)
                binding.layoutMentorship.tvRatingValue.text = "${firstReview.ratingMentorship}.0/5.0"
            }

            binding.rvStudentReviews.adapter = ReviewAdapter(companyReviews) {
                // Handle click jika diperlukan nanti
            }
        }

        // Tombol Kembali
        binding.ivBackDetail.setOnClickListener {
            finish()
        }

        // Set up Rating Labels (🌟 New according to updated layout)
        binding.layoutCulture.tvRatingLabel.text = "Culture"
        binding.layoutWorkload.tvRatingLabel.text = "Workload"
        binding.layoutMentorship.tvRatingLabel.text = "Mentorship"

        // Tombol Tulis Ulasan (Pindah ke AddReviewActivity)
        binding.btnWriteReview.setOnClickListener {
            val intent = Intent(this, com.agartha.didik.ui.review.AddReviewActivity::class.java).apply {
                putExtra("company", companyName)
            }
            startActivity(intent)
        }
    }
}