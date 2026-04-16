package com.agartha.didik

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment

/**
 * Fragment yang menampilkan layar pembuka (Splash Screen) saat aplikasi pertama kali dijalankan.
 * Berfungsi untuk menampilkan branding sebelum masuk ke konten utama.
 */
class SplashFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Menampilkan layout fragment_splash
        return inflater.inflate(R.layout.fragment_splash, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Menjalankan tugas tertunda (Delay) selama 2 detik (2000 milidetik)
        Handler(Looper.getMainLooper()).postDelayed({
            // Berpindah dari Splash Screen ke WelcomeFragment menggunakan FragmentTransaction
            parentFragmentManager.beginTransaction()
                .replace(R.id.fragment_container, WelcomeFragment())
                .commit()
        }, 2000)
    }
}
