package com.example.soundmood.ui.fragment.profilepage

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatDelegate
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.example.soundmood.databinding.FragmentProfilepagefragmentBinding
import com.example.soundmood.ui.ViewModelFactory
import com.example.soundmood.ui.loginpage.LoginActivity
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch

class ProfilePageFragment : Fragment() {
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
        val swithTheme = binding.switchTheme
        lifecycleScope.launchWhenStarted {
            profilePageViewModel.getTheme.collect { isDarkMode ->
                swithTheme.isChecked = isDarkMode
                applyTheme(isDarkMode)
            }
        }
        swithTheme.setOnCheckedChangeListener { _, isChecked ->
            profilePageViewModel.saveTheme(isChecked)
        }
        return binding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.linearlayoutLogout.setOnClickListener {
            logout()
        }
    }
    private fun applyTheme(isDarkMode: Boolean) {
        val mode = if (isDarkMode) {
            AppCompatDelegate.MODE_NIGHT_YES
        } else {
            AppCompatDelegate.MODE_NIGHT_NO
        }
        AppCompatDelegate.setDefaultNightMode(mode)
    }

    private fun logout() {
        profilePageViewModel.logout()
        navigateToLogin()
    }

    private fun navigateToLogin() {
        startActivity(Intent(requireContext(),LoginActivity::class.java))
    }
}