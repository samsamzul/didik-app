package com.agartha.didik.ui.profile

import android.os.Bundle
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.agartha.didik.R
import com.agartha.didik.databinding.ActivityEditProfileBinding
import com.agartha.didik.ui.ViewModelFactory
import com.agartha.didik.utils.PreferenceManager

class EditProfileActivity : AppCompatActivity() {

    private lateinit var binding: ActivityEditProfileBinding
    private lateinit var pref: PreferenceManager
    private val viewModel: EditProfileViewModel by viewModels {
        ViewModelFactory.getInstance(this)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityEditProfileBinding.inflate(layoutInflater)
        setContentView(binding.root)

        ViewCompat.setOnApplyWindowInsetsListener(binding.main) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        pref = PreferenceManager(this)
        val userId = pref.getUserId()

        if (userId == -1) {
            Toast.makeText(this, "Sesi berakhir, silakan login kembali", Toast.LENGTH_SHORT).show()
            finish()
            return
        }

        setupUI()
        observeViewModel()

        viewModel.getUserProfile(userId)
    }

    private fun setupUI() {
        binding.btnBack.setOnClickListener {
            onBackPressedDispatcher.onBackPressed()
        }

        binding.btnSave.setOnClickListener {
            val name = binding.edName.text.toString().trim()
            val email = binding.edEmail.text.toString().trim()

            if (name.isEmpty() || email.isEmpty()) {
                Toast.makeText(this, "Nama dan Email tidak boleh kosong", Toast.LENGTH_SHORT).show()
            } else {
                viewModel.updateProfile(pref.getUserId(), name, email)
            }
        }
    }

    private fun observeViewModel() {
        viewModel.userProfile.observe(this) { user ->
            if (user != null) {
                binding.edName.setText(user.namaLengkap)
                binding.edEmail.setText(user.email)
            }
        }

        viewModel.updateStatus.observe(this) { success ->
            if (success) {
                // Update local session name if it changed
                val newName = binding.edName.text.toString().trim()
                pref.saveUserName(newName)

                Toast.makeText(this, "Profil berhasil diperbarui", Toast.LENGTH_SHORT).show()
                finish()
            } else {
                Toast.makeText(this, "Gagal memperbarui profil", Toast.LENGTH_SHORT).show()
            }
        }
    }
}