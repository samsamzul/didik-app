package com.agartha.didik.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.agartha.didik.data.ReviewModel
import com.agartha.didik.databinding.ItemReviewBinding

class ReviewAdapter(
    private val listReview: List<ReviewModel>,
    private val onItemClick: (ReviewModel) -> Unit
) : RecyclerView.Adapter<ReviewAdapter.ViewHolder>() {

    class ViewHolder(val binding: ItemReviewBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemReviewBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val review = listReview[position]
        holder.binding.tvCompany.text = review.companyName
        holder.binding.tvPosition.text = review.position
        holder.binding.ratingBar.rating = review.rating
        // Assuming reviewerName is not in the model yet, we can use a placeholder or update the model
        // For now, let's just keep the default from XML or set a placeholder
        holder.binding.tvReviewerName.text = review.reviewerName

        holder.itemView.setOnClickListener {
            onItemClick(review)
        }
    }




    override fun getItemCount(): Int = listReview.size
}
