package com.agartha.didik.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.agartha.didik.databinding.ItemReviewBinding

/**
 * Kerangka Template Adapter untuk RecyclerView.
 * Siap digunakan dan disesuaikan dengan data model asli nanti.
 */
class ReviewAdapter(
    private val listData: List<Any> = emptyList(), // Default kosongan menggunakan tipe umum Any
    private val onItemClick: ((Any) -> Unit)? = null // Callback opsional bisa di-set null dulu
) : RecyclerView.Adapter<ReviewAdapter.ViewHolder>() {

    /**
     * ViewHolder mengikat layout item review.
     */
    class ViewHolder(val binding: ItemReviewBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemReviewBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        // Biarkan blok ini kosongan dulu dari logika set text data asli, Nad.
        // Nanti temen kelompokmu tinggal masukin data binding-nya di sini.

        holder.itemView.setOnClickListener {
            if (listData.isNotEmpty()) {
                onItemClick?.invoke(listData[position])
            }
        }

    }

    // Mengembalikan ukuran listData, jika kosongan otomatis mengembalikan 0
    override fun getItemCount(): Int = listData.size
}