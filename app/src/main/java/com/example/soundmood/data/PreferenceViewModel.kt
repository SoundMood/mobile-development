package com.example.soundmood.data

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch

class PreferenceViewModel(private val repository: PreferenceRepository):ViewModel() {

    // Flow untuk accsess token spotify
    val accsessToken: Flow<String?> = repository.accsessTokenFlow

    // Flow untuk app token
    val appToken : Flow<String?> = repository.appTokenFlow

    // Flow untuk access token spotify expiry
    val accessTokenExpiry : Flow<Long> = repository.accessTokenExpiry

    // Flow untuk app token expiry
    val appTokenExpiry : Flow<Long> = repository.appTokenExpiry

    // Flow untuk dark mode
    val darkMode : Flow<Boolean> = repository.darkModeFlow

    // Menyimpan accsess token
    fun saveAccsessToken(token:String){
        viewModelScope.launch {
            repository.saveAccessToken(token)
        }
    }

    // Menghapus access token
    fun clearAccsessToken(token:String){
        viewModelScope.launch {
            repository.clearAccessToken(token)
        }
    }

    // Menyimpan app token
    fun saveAppToken(appToken:String){
        viewModelScope.launch {
            repository.saveAppToken(appToken)
        }
    }

    // Mengambil app token
    fun getAppToken():String?{
        var token : String? = null
        viewModelScope.launch {
            token = appToken.first()
        }
        return token
    }

    // Clear app token
    fun clearAppToken(token:String){
        viewModelScope.launch {
            repository.clearAppToken(token)
        }

    }

    // Menyimpan access token expiry
    fun saveAccessTokenExpiry(expiry:Long){
        viewModelScope.launch {
            repository.saveAccesTokenExpiry(expiry)
        }
    }

    // Menyimpan app token expiry
    fun saveAppTokenExpiry(expiry:Long){
        viewModelScope.launch {
            repository.saveAppTokenExpiry(expiry)
        }
    }

    // Menyimpan dark mode
    fun saveDarkMode(isDark:Boolean){
        viewModelScope.launch {
            repository.saveDarkMode(isDark)
        }
    }
}