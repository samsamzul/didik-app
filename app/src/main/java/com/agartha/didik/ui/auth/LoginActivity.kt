package com.agartha.didik.ui.auth

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.agartha.didik.databinding.ActivityLoginBinding
import com.agartha.didik.ui.main.MainActivity
import com.agartha.didik.ui.ViewModelFactory
import com.agartha.didik.utils.PreferenceManager

class LoginActivity : AppCompatActivity() {

    private lateinit var binding: ActivityLoginBinding
    private lateinit var viewModel: AuthViewModel
    private lateinit var preferenceManager: PreferenceManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        preferenceManager = PreferenceManager(this)

        val factory = ViewModelFactory.getInstance(this)
        viewModel = ViewModelProvider(this, factory)[AuthViewModel::class.java]

        setupObservers()

        // Tombol Login diklik
        binding.btnLogin.setOnClickListener {
            val email = binding.etLoginEmail.text.toString().trim()
            val password = binding.etPassword.text.toString().trim()

            if (email.isEmpty() || password.isEmpty()) {
                Toast.makeText(this, "Email dan Password tidak boleh kosong!", Toast.LENGTH_SHORT).show()
            } else {
                viewModel.login(email, password)
            }
        }

        // Teks "Daftar di sini" diklik
        binding.tvToRegister.setOnClickListener {
            startActivity(Intent(this, RegisterActivity::class.java))
            finish()
        }
    }

    private fun setupObservers() {
        viewModel.authState.observe(this) { state ->
            when (state) {
                is AuthState.Loading -> { }
                is AuthState.LoginSuccess -> {
                    // SIMPAN SESSION USER YANG BERHASIL LOGIN
                    preferenceManager.setLoginStatus(true)
                    preferenceManager.saveUserId(state.userId)
                    preferenceManager.saveUserName(state.userName)

                    Toast.makeText(this, "Login Berhasil! Selamat Datang.", Toast.LENGTH_SHORT).show()

                    // Pindah ke Dashboard Utama
                    startActivity(Intent(this, MainActivity::class.java))
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