package com.agartha.didik.ui.review

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.agartha.didik.databinding.ItemDetailreviewBinding // Sesuaikan dengan nama layoutmu

class ReviewDetailFragment : Fragment() {

    private var _binding: ItemDetailreviewBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = ItemDetailreviewBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Ambil data yang dikirim dari adapter (misal via Parcelable atau String)
        // val reviewData = arguments?.getParcelable<ReviewModel>("EXTRA_REVIEW")

        // Isi UI detail di sini...

        binding.btnBack.setOnClickListener {
            parentFragmentManager.popBackStack()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}