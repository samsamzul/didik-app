package com.agartha.didik

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.agartha.didik.databinding.FragmentDetailReviewBinding

/**
 * Fragment yang menampilkan rincian lengkap dari sebuah review.
 * Data diterima melalui Bundle (Arguments).
 */
class DetailReviewFragment : Fragment() {

    private var _binding: FragmentDetailReviewBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentDetailReviewBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // 1. Mengambil data yang dikirim dari DashboardFragment melalui Bundle
        val company = arguments?.getString("company")
        val position = arguments?.getString("position")
        val rating = arguments?.getFloat("rating") ?: 0f
        val jobDesc = arguments?.getString("job")
        val reviewText = arguments?.getString("review")
        val reviewer = arguments?.getString("reviewer")

        // 2. Menampilkan data tersebut ke komponen UI di layar detail
        binding.tvDetailCompany.text = company
        binding.tvDetailPosition.text = position
        binding.rbDetail.rating = rating
        binding.tvDetailJobDesc.text = jobDesc
        binding.tvDetailReview.text = reviewText
        binding.tvDetailReviewer.text = "Reviewed by: $reviewer"

        // 3. Logika tombol kembali ke halaman sebelumnya
        binding.ivBack.setOnClickListener {
            parentFragmentManager.popBackStack()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        // Mencegah memory leak saat Fragment dihancurkan
        _binding = null
    }
}
