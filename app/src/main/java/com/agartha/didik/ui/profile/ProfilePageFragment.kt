package com.agartha.didik.ui.profile

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.fragment.app.activityViewModels
import com.agartha.didik.databinding.FragmentProfilePageBinding
import com.agartha.didik.ui.ViewModelFactory
import com.agartha.didik.ui.auth.LoginActivity
import com.agartha.didik.ui.review.ReviewViewModel
import com.agartha.didik.utils.PreferenceManager

class ProfilePageFragment : Fragment() {

    private var _binding: FragmentProfilePageBinding? = null
    private val binding get() = _binding!!
    private lateinit var preferenceManager: PreferenceManager

    private val reviewViewModel: ReviewViewModel by activityViewModels {
        ViewModelFactory.getInstance(requireContext())
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentProfilePageBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        preferenceManager = PreferenceManager(requireContext())

        setupProfileData()
        setupAction()
        observeReviewData()
    }

    override fun onResume() {
        super.onResume()
        setupProfileData()
        reviewViewModel.loadReviewsFromDatabase() // Refresh data saat balik ke profil
    }

    private fun observeReviewData() {
        val currentUserId = preferenceManager.getUserId()
        reviewViewModel.reviews.observe(viewLifecycleOwner) { reviews ->
            val userReviewCount = reviews.count { it.userId == currentUserId }
            binding.tvReviewCount.text = userReviewCount.toString()
        }
    }

    private fun setupProfileData() {
        val userName = preferenceManager.getUserName()
        if (!userName.isNullOrEmpty()) {
            binding.tvNameLarge.text = userName
        }
    }

    private fun setupAction() {
        binding.cvEditProfile.setOnClickListener {
            val intent = Intent(requireContext(), EditProfileActivity::class.java)
            startActivity(intent)
        }

        binding.btnLogout.setOnClickListener {
            preferenceManager.clearSession()
            Toast.makeText(requireContext(), "Berhasil Keluar", Toast.LENGTH_SHORT).show()
            
            val intent = Intent(requireActivity(), LoginActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            startActivity(intent)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
