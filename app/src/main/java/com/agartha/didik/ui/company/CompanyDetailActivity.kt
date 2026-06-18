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

    override fun onResume() {
        super.onResume()
        viewModel.loadReviewsFromDatabase()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Inisialisasi View Binding asli Activity murni sesuai peta XML
        binding = ActivityCompanyDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Ambil data Intent kiriman dari ExploreFragment
        val companyId = intent.getIntExtra("company_id", 1)
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

            binding.rvStudentReviews.adapter = ReviewAdapter(
                listReview = companyReviews,
                onItemClick = { review ->
                    // Edit Review
                    val editIntent = Intent(this, com.agartha.didik.ui.review.AddReviewActivity::class.java).apply {
                        putExtra("EXTRA_REVIEW_ID", review.reviewId)
                        putExtra("company_id", review.companyId)
                        putExtra("EXTRA_COMPANY", review.companyName)
                        putExtra("EXTRA_POSITION", review.position)
                        putExtra("EXTRA_REVIEW_TEXT", review.reviewText)
                        putExtra("EXTRA_RATING", review.rating.toInt())
                        putExtra("EXTRA_WORKLOAD", review.ratingWorkload)
                        putExtra("EXTRA_MENTORSHIP", review.ratingMentorship)
                        putExtra("EXTRA_CULTURE", review.ratingCulture)
                        putExtra("EXTRA_PROS", review.pros)
                        putExtra("EXTRA_CONS", review.cons)
                        putExtra("EXTRA_IS_ANONIM", review.isAnonim)
                    }
                    startActivity(editIntent)
                },
                onDeleteClick = { review ->
                    // Hapus Review
                    val ulasanEntity = com.agartha.didik.data.local.entity.UlasanEntity(
                        id = review.reviewId,
                        userId = review.userId,
                        perusahaanId = review.companyId,
                        namaPosisi = review.position,
                        deskripsiKerja = review.jobDesc,
                        ratingWorkload = review.ratingWorkload,
                        ratingMentorship = review.ratingMentorship,
                        ratingCulture = review.ratingCulture,
                        teksUlasan = review.reviewText,
                        ilmuDidapat = "", // Placeholder
                        poinKelebihan = review.pros,
                        poinKekurangan = review.cons,
                        isAnonim = review.isAnonim
                    )
                    viewModel.deleteReview(ulasanEntity)
                    Toast.makeText(this, "Review deleted", Toast.LENGTH_SHORT).show()
                }
            )
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
                putExtra("company_id", companyId)
                putExtra("company", companyName)
            }
            startActivity(intent)
        }
    }
}
