package com.example.soundmood.ui.fragment.profilepage

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import com.example.soundmood.databinding.FragmentProfilepagefragmentBinding
import com.example.soundmood.ui.ViewModelFactory
import com.example.soundmood.ui.loginpage.LoginActivity

class ProfilePageFragment : Fragment() {
    // TODO: Rename and change types of parameters
    private var _binding : FragmentProfilepagefragmentBinding ?=null
    private val binding get() = _binding!!

    private val profilePageViewModel : ProfilePageViewModel by viewModels {
        ViewModelFactory(requireContext())
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentProfilepagefragmentBinding.inflate(inflater,container,false)
        return binding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.linearlayoutLogout.setOnClickListener {
            logout()
        }
    }

    private fun logout() {
        val accessToken = profilePageViewModel.accessToken.toString()
        profilePageViewModel.logout(accessToken)
        navigateToLogin()
    }

    private fun navigateToLogin() {
        startActivity(Intent(requireContext(),LoginActivity::class.java))
    }
}