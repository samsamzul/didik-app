package com.agartha.didik

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.agartha.didik.databinding.FragmentLoginBinding

/**
 * Fragment yang menangani proses masuk (Login) pengguna ke dalam aplikasi.
 */
class LoginFragment : Fragment() {
    private var _binding: FragmentLoginBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentLoginBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Mengambil referensi SharedPreferences untuk mengecek data registrasi
        val sharedPref = requireActivity().getSharedPreferences("UserPrefs", Context.MODE_PRIVATE)

        // Logika saat tombol Login diklik
        binding.btnLogin.setOnClickListener {
            val inputUser = binding.etUsername.text.toString().trim()
            val inputPass = binding.etPassword.text.toString().trim()

            // Validasi input: Tidak boleh ada kolom yang kosong
            if (inputUser.isEmpty() || inputPass.isEmpty()) {
                Toast.makeText(requireContext(), "Harap isi email dan password!", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            // Mencocokkan input dengan data email dan password yang tersimpan saat Registrasi
            val savedEmail = sharedPref.getString("reg_email", "")
            val savedPass = sharedPref.getString("reg_pass", "")

            // Logika verifikasi kredensial
            if (inputUser == savedEmail && inputPass == savedPass) {
                // Berhasil Login!
                // Ambil Nama Asli dari data registrasi untuk sesi sapaan di Dashboard
                val realName = sharedPref.getString("reg_user", "User")
                sharedPref.edit().putString("user_name", realName).apply()

                // Pindah ke halaman DashboardFragment
                parentFragmentManager.beginTransaction()
                    .replace(R.id.fragment_container, DashboardFragment())
                    .commit()
            } else {
                // Gagal Login: Tampilkan pesan kesalahan
                Toast.makeText(requireContext(), "Email atau Password salah!", Toast.LENGTH_SHORT).show()
            }
        }

        // Navigasi ke halaman Registrasi jika user belum memiliki akun
        binding.tvToRegister.setOnClickListener {
            parentFragmentManager.beginTransaction()
                .replace(R.id.fragment_container, RegisterFragment())
                .addToBackStack(null)
                .commit()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        // Membersihkan binding untuk mencegah kebocoran memori
        _binding = null
    }
}
