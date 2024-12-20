package com.example.soundmood.ui

import android.app.Application
import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.soundmood.data.PreferenceRepository
import com.example.soundmood.data.PreferenceViewModel
import com.example.soundmood.ui.captureimagepage.CaptureViewModel
import com.example.soundmood.ui.fragment.historypage.HistoryPageViewModel
import com.example.soundmood.ui.fragment.homepage.HomePageViewModel
import com.example.soundmood.ui.fragment.profilepage.ProfilePageViewModel
import com.example.soundmood.ui.loginpage.LoginViewModel
import com.example.soundmood.ui.moodplaylistgenerated.MoodPlaylistGeneratedViewModel
import com.example.soundmood.ui.moodresultpage.MoodResultViewModel

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
            modelClass.isAssignableFrom(CaptureViewModel::class.java)->{
                CaptureViewModel(preferenceViewModel) as T
            }
            modelClass.isAssignableFrom(MoodResultViewModel::class.java)->{
                MoodResultViewModel(preferenceViewModel) as T
            }
            modelClass.isAssignableFrom(MoodPlaylistGeneratedViewModel::class.java)->{
                MoodPlaylistGeneratedViewModel(preferenceViewModel) as T
            }
            modelClass.isAssignableFrom(HistoryPageViewModel::class.java)->{
                HistoryPageViewModel(preferenceViewModel) as T
            }
            else -> throw IllegalArgumentException("Unknown ViewModel class: ${modelClass.name}")
        }
    }
}