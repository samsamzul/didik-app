package com.agartha.didik.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.agartha.didik.databinding.ItemCompanyCardBinding
import com.agartha.didik.ui.review.ReviewModel

class CompanyCardAdapter(
    private val listCompany: List<ReviewModel>,
    private val onItemClick: (ReviewModel) -> Unit
) : RecyclerView.Adapter<CompanyCardAdapter.ViewHolder>() {

    class ViewHolder(val binding: ItemCompanyCardBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemCompanyCardBinding.inflate(
            LayoutInflater.from(parent.context), 
            parent, 
            false
        )
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = listCompany[position]
        with(holder.binding) {
            tvTitle.text = item.position
            tvCompany.text = "${item.companyName} - ${item.location}"
            tvTag1.text = item.category
            // Tag 2 bisa diisi dengan info lain, misal tipe kerja
            tvTag2.text = "Full Time" 
            
            root.setOnClickListener {
                onItemClick(item)
            }
        }
    }

    override fun getItemCount(): Int = listCompany.size
}
