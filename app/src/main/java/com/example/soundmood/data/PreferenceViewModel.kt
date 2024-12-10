package com.example.soundmood.data

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch

class PreferenceViewModel(private val repository: PreferenceRepository):ViewModel() {

    // Flow untuk accsess token
    val accsessToken: Flow<String?> = repository.accsessTokenFlow

    // Flow untuk dark mode
    val darkMode : Flow<Boolean> = repository.darkModeFlow

    // Menyimpan accsess token
    fun saveAccsessToken(token:String){
        viewModelScope.launch {
            repository.saveAccessToken(token)
        }
    }
    // Menyimpan dark mode
    fun saveDarkMode(isDark:Boolean){
        viewModelScope.launch {
            repository.saveDarkMode(isDark)
        }
    }
}