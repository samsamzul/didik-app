package com.agartha.didik

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.agartha.didik.data.RegisterModel
import com.agartha.didik.databinding.FragmentRegisterBinding

/**
 * Fragment yang menangani proses pendaftaran akun baru bagi pengguna (Registrasi).
 */
class RegisterFragment : Fragment() {
    private var _binding: FragmentRegisterBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentRegisterBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Logika saat tombol Register diklik
        binding.btnRegisterSubmit.setOnClickListener {
            // 1. Mengambil data input dari EditText dan membersihkan spasi berlebih
            val name = binding.etRegisterName.text.toString().trim()
            val email = binding.etRegisterEmail.text.toString().trim()
            val pass = binding.etRegisterPass.text.toString().trim()

            // 2. Membungkus data ke dalam RegisterModel
            val newUser = RegisterModel(name, email, pass)

            // 3. Validasi: Memastikan semua field sudah terisi
            if (newUser.name.isNotEmpty() && newUser.email.isNotEmpty() && newUser.password.isNotEmpty()) {

                // Menyimpan data pendaftaran secara lokal menggunakan SharedPreferences
                val sharedPref = requireActivity().getSharedPreferences("UserPrefs", Context.MODE_PRIVATE)
                val editor = sharedPref.edit()

                editor.putString("reg_user", newUser.name)
                editor.putString("reg_email", newUser.email)
                editor.putString("reg_pass", newUser.password)
                editor.apply()

                // Memberikan notifikasi sukses kepada user
                Toast.makeText(requireContext(), "Akun berhasil dibuat! Silakan Login", Toast.LENGTH_SHORT).show()

                // Otomatis kembali ke halaman Login setelah berhasil mendaftar
                parentFragmentManager.popBackStack()
            } else {
                // Notifikasi jika ada data yang belum lengkap
                Toast.makeText(requireContext(), "Lengkapi semua field terlebih dahulu!", Toast.LENGTH_SHORT).show()
            }
        }

        // Tombol untuk kembali ke halaman Login secara manual
        binding.tvToLogin?.setOnClickListener {
            parentFragmentManager.popBackStack()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
