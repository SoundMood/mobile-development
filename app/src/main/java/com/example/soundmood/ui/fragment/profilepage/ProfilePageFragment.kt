package com.example.soundmood.ui.fragment.profilepage

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatDelegate
import androidx.fragment.app.viewModels
import com.bumptech.glide.Glide
import com.example.soundmood.databinding.FragmentProfilepagefragmentBinding
import com.example.soundmood.ui.ViewModelFactory
import com.example.soundmood.ui.loginpage.LoginActivity

class ProfilePageFragment : Fragment() {
    private var _binding : FragmentProfilepagefragmentBinding ?=null
    private val binding get() = _binding!!

    private var userId: String? = null
    private var userName: String? = null
    private var userImage: String? = null

    private val profilePageViewModel : ProfilePageViewModel by viewModels {
        ViewModelFactory(requireContext())
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        arguments?.let {
            userId = it.getString("user_id")
            userName = it.getString("user_name")
            userImage = it.getString("user_image")
        }

        _binding = FragmentProfilepagefragmentBinding.inflate(inflater,container,false)
        return binding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.linearlayoutLogout.setOnClickListener {
            logout()
        }

        binding.tvUserName.text = userName
        binding.tvUserId.text = userId
        Glide.with(requireContext().applicationContext)
            .load(userImage)
            .circleCrop()
            .into(binding.imageviewProfile)
    }

    private fun logout() {
        profilePageViewModel.logout()
        navigateToLogin()
    }

    private fun navigateToLogin() {
        startActivity(Intent(requireContext(),LoginActivity::class.java))
    }
}