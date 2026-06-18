package com.agartha.didik.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.agartha.didik.databinding.ItemReviewBinding
import com.agartha.didik.ui.review.ReviewModel

/**
 * Adapter untuk mengelola dan menampilkan daftar review dalam RecyclerView.
 * Menghubungkan data [ReviewModel] dengan tampilan layout [ItemReviewBinding].
 */
class ReviewAdapter(
    private val listReview: List<ReviewModel>, // Daftar data review yang akan ditampilkan
    private val onItemClick: (ReviewModel) -> Unit // Callback saat item diklik
) : RecyclerView.Adapter<ReviewAdapter.ViewHolder>() {

    /**
     * ViewHolder bertugas menyimpan referensi view untuk setiap item dalam daftar.
     */
    class ViewHolder(val binding: ItemReviewBinding) : RecyclerView.ViewHolder(binding.root)

    /**
     * Membuat tampilan item baru (dipanggil saat RecyclerView membutuhkan item baru).
     */
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemReviewBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    /**
     * Menghubungkan data review dengan komponen UI di dalam ViewHolder.
     */
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val review = listReview[position]
        
        // Mengisi data ke dalam komponen UI
        holder.binding.tvReviewerName.text = review.reviewerName
        holder.binding.tvPositionAndCompany.text = "${review.position} @ ${review.companyName}"
        holder.binding.ratingBar.rating = review.rating
        holder.binding.tvReviewTitle.text = review.jobDesc
        holder.binding.tvReviewContent.text = review.reviewText
        holder.binding.tvTagCategory.text = review.category

        // Menangani aksi klik pada item
        holder.itemView.setOnClickListener {
            onItemClick(review)
        }
    }


    /**
     * Mengembalikan jumlah total item dalam daftar.
     */
    override fun getItemCount(): Int = listReview.size
}
