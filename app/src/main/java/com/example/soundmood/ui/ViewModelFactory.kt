package com.example.soundmood.ui

import android.app.Application
import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.soundmood.data.PreferenceRepository
import com.example.soundmood.data.PreferenceViewModel
import com.example.soundmood.ui.fragment.homepage.HomePageViewModel

class ViewModelFactory(private val context: Context) : ViewModelProvider.Factory {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        val repository = PreferenceRepository(context)
        return when {
            modelClass.isAssignableFrom(PreferenceViewModel::class.java) -> {
                PreferenceViewModel(repository) as T
            }
            modelClass.isAssignableFrom(HomePageViewModel::class.java) -> {
                val preferenceViewModel = PreferenceViewModel(repository)
                HomePageViewModel(preferenceViewModel) as T
            }
            else -> throw IllegalArgumentException("Unknown ViewModel class: ${modelClass.name}")
        }
    }
}