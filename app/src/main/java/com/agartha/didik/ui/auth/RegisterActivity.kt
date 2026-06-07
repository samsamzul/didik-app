package com.agartha.didik.ui.auth

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.agartha.didik.databinding.ActivityRegisterBinding
import com.agartha.didik.ui.ViewModelFactory

class RegisterActivity : AppCompatActivity() {

    private lateinit var binding: ActivityRegisterBinding
    private lateinit var viewModel: AuthViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRegisterBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val factory = ViewModelFactory.getInstance(this)
        viewModel = ViewModelProvider(this, factory)[AuthViewModel::class.java]

        setupObservers()

        binding.btnRegister.setOnClickListener {
            val nama = binding.etRegisterName.text.toString().trim()
            val email = binding.etRegisterEmail.text.toString().trim()
            val instansi = binding.etRegisterInstansi.text.toString().trim()
            val password = binding.etRegisterPassword.text.toString().trim()

            // 1. Pola Regex untuk memastikan email wajib berakhiran .ac.id
            val emailPattern = "^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.ac\\.id$"

            if (nama.isEmpty() || email.isEmpty() || instansi.isEmpty() || password.isEmpty()) {
                Toast.makeText(this, "Semua kolom wajib diisi!", Toast.LENGTH_SHORT).show()
            }
            // 2. Cek apakah format emailnya sudah sesuai .ac.id atau belum
            else if (!email.matches(emailPattern.toRegex())) {
                binding.etRegisterEmail.error = "Wajib menggunakan email mahasiswa (.ac.id)!"
                Toast.makeText(this, "Format email harus menggunakan akun kampus (.ac.id)!", Toast.LENGTH_SHORT).show()
            }
            // 3. Kalau semua aman, baru lolos kirim ke ViewModel
            else {
                viewModel.register(nama, email, password, instansi)
            }
        }

        binding.tvToLogin.setOnClickListener {
            startActivity(Intent(this, LoginActivity::class.java))
            finish()
        }
    }

    private fun setupObservers() {
        viewModel.authState.observe(this) { state ->
            when (state) {
                is AuthState.Loading -> { }
                is AuthState.Success -> {
                    Toast.makeText(this, state.message, Toast.LENGTH_LONG).show()
                    startActivity(Intent(this, LoginActivity::class.java))
                    finish()
                }
                is AuthState.Error -> {
                    Toast.makeText(this, state.error, Toast.LENGTH_LONG).show()
                }
                else -> {}
            }
        }
    }
}