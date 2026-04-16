package com.agartha.didik

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.agartha.didik.databinding.ActivityMainBinding

/**
 * Activity utama aplikasi yang berfungsi sebagai container (wadah) bagi semua Fragment.
 * Menggunakan arsitektur "Single Activity" di mana perpindahan layar dilakukan melalui Fragment.
 */
class MainActivity : AppCompatActivity() {
    
    // Inisialisasi View Binding untuk mengakses layout activity_main
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        
        // Menyiapkan layout menggunakan View Binding
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Memeriksa apakah aplikasi baru saja dijalankan (bukan karena rotasi layar)
        if (savedInstanceState == null) {
            // Menampilkan SplashFragment sebagai layar pertama saat aplikasi dibuka
            supportFragmentManager.beginTransaction()
                .replace(R.id.fragment_container, SplashFragment())
                .commit()
        }
    }
}
