package com.example.soundmood.ui.fragment.profilepage

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.soundmood.data.PreferenceViewModel
import com.example.soundmood.network.ApiConfig
import kotlinx.coroutines.launch

class ProfilePageViewModel(private val preferenceViewModel: PreferenceViewModel) : ViewModel() {
    val accessToken = preferenceViewModel.accsessToken
    val appToken = preferenceViewModel.appToken



    fun logout(){
        viewModelScope.launch {
            preferenceViewModel.clearAccsessToken(accessToken.toString())
            preferenceViewModel.clearAppToken(appToken.toString())
        }
    }
    val getTheme = preferenceViewModel.darkMode
    fun saveTheme(isDarkMode : Boolean){
        viewModelScope.launch {
            preferenceViewModel.saveDarkMode(isDarkMode)
        }
    }

}