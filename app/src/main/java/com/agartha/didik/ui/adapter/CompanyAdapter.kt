package com.agartha.didik.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.agartha.didik.databinding.ItemCompanyBinding
import com.agartha.didik.ui.review.ReviewModel
import java.util.Locale

class CompanyAdapter(
    private val listCompany: List<ReviewModel>,
    private val onItemClick: (ReviewModel) -> Unit
) : RecyclerView.Adapter<CompanyAdapter.ViewHolder>() {

    class ViewHolder(val binding: ItemCompanyBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemCompanyBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = listCompany[position]
        with(holder.binding) {
            tvTitle.text = item.position
            tvCompany.text = "${item.companyName} - ${item.location}"
            tvRating.text = String.format(Locale.getDefault(), "★ %.1f", item.rating)
            tvDescription.text = item.jobDesc
            
            // tvTime sementara di-hardcode karena ReviewModel belum memiliki field waktu/tanggal
            tvTime.text = "Baru saja"

            root.setOnClickListener {
                onItemClick(item)
            }
        }
    }

    override fun getItemCount(): Int = listCompany.size
}
