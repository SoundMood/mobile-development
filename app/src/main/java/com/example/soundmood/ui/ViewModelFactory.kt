package com.example.soundmood.ui

import android.app.Application
import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.soundmood.data.PreferenceRepository
import com.example.soundmood.data.PreferenceViewModel
import com.example.soundmood.ui.fragment.homepage.HomePageViewModel
import com.example.soundmood.ui.fragment.profilepage.ProfilePageViewModel
import com.example.soundmood.ui.loginpage.LoginViewModel

class ViewModelFactory(private val context: Context) : ViewModelProvider.Factory {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        val repository = PreferenceRepository(context)
        val preferenceViewModel = PreferenceViewModel(repository)
        return when {
            modelClass.isAssignableFrom(PreferenceViewModel::class.java) -> {
                PreferenceViewModel(repository) as T
            }
            modelClass.isAssignableFrom(HomePageViewModel::class.java) -> {
                HomePageViewModel(preferenceViewModel) as T
            }
            modelClass.isAssignableFrom(ProfilePageViewModel::class.java) -> {
                ProfilePageViewModel(preferenceViewModel) as T
            }
            modelClass.isAssignableFrom(LoginViewModel::class.java)->{
                LoginViewModel(preferenceViewModel) as T
            }
            else -> throw IllegalArgumentException("Unknown ViewModel class: ${modelClass.name}")
        }
    }
}