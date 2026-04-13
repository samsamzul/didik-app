package com.agartha.didik

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.agartha.didik.data.LoginModel
import com.agartha.didik.databinding.ActivityLoginBinding

class LoginActivity : AppCompatActivity() {
    private lateinit var binding: ActivityLoginBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)
        
        ViewCompat.setOnApplyWindowInsetsListener(binding.main) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        // Di dalam onCreate LoginActivity
        binding.btnLogin.setOnClickListener {
            // 1. Ambil input
            val inputUser = binding.etUsername.text.toString()
            val inputPass = binding.etPassword.text.toString()

            // 2. Bungkus ke dalam Data Class
            val loginData = LoginModel(inputUser, inputPass)

            // 3. Logika pengecekan (Contoh simpel)
            if (loginData.username.isNotEmpty() && loginData.password.isNotEmpty()) {

                // Simpan nama ke SharedPreferences biar bisa diambil Dashboard (tetap perlu ini)
                val sharedPref = getSharedPreferences("UserPrefs", Context.MODE_PRIVATE)
                sharedPref.edit().putString("user_name", loginData.username).apply()

                // Pindah halaman
                val intent = Intent(this, MainActivity::class.java)
                startActivity(intent)
                finish()
            } else {
                Toast.makeText(this, "Username dan Password harus diisi!", Toast.LENGTH_SHORT).show()
            }
        }
    }
}