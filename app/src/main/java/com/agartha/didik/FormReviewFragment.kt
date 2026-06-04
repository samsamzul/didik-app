package com.agartha.didik

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.agartha.didik.data.ReviewModel
import com.agartha.didik.databinding.FragmentFormReviewBinding

/**
 * Fragment yang menangani proses penulisan dan pengiriman review pekerjaan baru.
 */
class FormReviewFragment : Fragment() {

    private var _binding: FragmentFormReviewBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentFormReviewBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // 1. Logika Tombol Kembali (Biar bisa balik ke Dashboard)
        binding.ivBack.setOnClickListener {
            parentFragmentManager.popBackStack()
        }

        // 2. Logika Tombol Simpan/Kirim Review
        binding.btnSave.setOnClickListener {
            // Mengambil input dari semua kolom teks dan menghapus spasi berlebih
            val company = binding.etCompanyName.text.toString().trim()
            val position = binding.etPosition.text.toString().trim()
            val jobDesc = binding.etJobDesc.text.toString().trim()
            val reviewText = binding.etReviewText.text.toString().trim()
            val rating = binding.rbReview.rating

            // Validasi Keamanan: Pastikan semua field wajib sudah terisi dan bintang sudah dipilih
            if (company.isNotEmpty() && position.isNotEmpty() && jobDesc.isNotEmpty() && 
                reviewText.isNotEmpty() && rating > 0f) {

                // Ambil data user yang sedang aktif untuk dicatat sebagai penulis review
                val sharedPref = requireActivity().getSharedPreferences("UserPrefs", Context.MODE_PRIVATE)
                val reviewerName = sharedPref.getString("user_name", "User") ?: "User"
                
                // Menghubungkan ke ViewModel untuk menyimpan data
                val viewModel = androidx.lifecycle.ViewModelProvider(requireActivity())[com.agartha.didik.data.ReviewViewModel::class.java]

                // Membungkus data ke dalam objek ReviewModel (ID dibuat secara acak untuk simulasi)
                val newReview = ReviewModel(
                    id = (0..1000).random(),
                    companyName = company,
                    position = position,
                    reviewerName = reviewerName,
                    jobDesc = jobDesc,
                    reviewText = reviewText,
                    rating = rating
                )

                // Simpan review ke data utama (ViewModel)
                viewModel.addReview(newReview)

                // Memberikan feedback visual sukses kepada user
                Toast.makeText(requireContext(), "Review untuk $company berhasil terkirim!", Toast.LENGTH_SHORT).show()

                // Otomatis kembali ke Dashboard setelah berhasil menyimpan
                parentFragmentManager.popBackStack()

            } else {
                // Tampilkan pesan bantuan spesifik jika ada form yang terlewat
                val message = when {
                    company.isEmpty() || position.isEmpty() || jobDesc.isEmpty() || reviewText.isEmpty() -> 
                        "Semua kolom teks harus diisi!"
                    rating <= 0f -> "Jangan lupa berikan rating bintangnya ya!"
                    else -> "Mohon lengkapi semua data review!"
                }
                Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show()
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
