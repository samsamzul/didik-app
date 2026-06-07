package com.agartha.didik.ui.auth

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.appcompat.app.AppCompatActivity
import com.agartha.didik.databinding.ActivitySplashBinding
import com.agartha.didik.ui.main.MainActivity
import com.agartha.didik.utils.PreferenceManager

@SuppressLint("CustomSplashScreen")
class SplashActivity : AppCompatActivity() { // <-- WAJIB AppCompatActivity, bukan Fragment!

    private lateinit var binding: ActivitySplashBinding
    private lateinit var preferenceManager: PreferenceManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Menggunakan View Binding bawaan Activity
        binding = ActivitySplashBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Inisialisasi preference manager buat cek session login
        preferenceManager = PreferenceManager(this)

        // Delay 2 detik (2000 milidetik)
        Handler(Looper.getMainLooper()).postDelayed({

            // Cek status login mahasiswa Didik
            if (preferenceManager.isLoggedIn()) {
                // Kalau sudah login, langsung bypass ke Dashboard Utama
                startActivity(Intent(this, MainActivity::class.java))
            } else {
                // Kalau belum login, arahkan ke WelcomeActivity
                startActivity(Intent(this, WelcomeActivity::class.java))
            }

            // Hancurkan SplashActivity agar kalau user pencet tombol 'Back' di HP, gak balik ke logo splash lagi
            finish()

        }, 2000L)
    }
}