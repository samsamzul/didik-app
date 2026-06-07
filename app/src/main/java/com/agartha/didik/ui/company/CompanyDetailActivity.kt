package com.agartha.didik.ui.company

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.agartha.didik.adapter.ReviewAdapter
import com.agartha.didik.databinding.ActivityCompanyDetailBinding // 🌟 SINKRON 100% SAMA PETA LAYOUT KAMU
import com.agartha.didik.ui.review.ReviewModel // 🌟 Ambil model yang kita buat menumpang di ViewModel tadi
import com.agartha.didik.ui.review.ReviewViewModel

class CompanyDetailActivity : AppCompatActivity() {

    private lateinit var binding: ActivityCompanyDetailBinding
    private val viewModel: ReviewViewModel by viewModels()

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
        binding.tvToolbarTitle.text = companyName
        binding.tvCompanyPosition.text = position
        binding.tvCompanyCategory.text = category

        // Set up RecyclerView ulasan siswa
        binding.rvStudentReviews.layoutManager = LinearLayoutManager(this)

        // Ambil data ulasan dari ViewModel
        viewModel.reviews.observe(this) { reviews ->
            val companyReviews = reviews.filter { it.companyName.toString() == companyName }
            binding.rvStudentReviews.adapter = ReviewAdapter(companyReviews) {
                // Handle click jika diperlukan nanti
            }
        }

        // Tombol Kembali
        binding.ivBackDetail.setOnClickListener {
            finish()
        }

        // Tombol Tulis Ulasan (Pindah ke AddReviewActivity sesuai peta kamu)
        binding.btnWriteReview.setOnClickListener {
            Toast.makeText(this, "Navigasi ke Form Review", Toast.LENGTH_SHORT).show()
        }

        // Tombol Apply
        binding.btnApply.setOnClickListener {
            Toast.makeText(this, "Redirecting to application page...", Toast.LENGTH_SHORT).show()
        }
    }
}