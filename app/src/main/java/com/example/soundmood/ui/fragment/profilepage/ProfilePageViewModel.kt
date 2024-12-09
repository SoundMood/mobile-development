package com.example.soundmood.ui.fragment.profilepage

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.soundmood.data.PreferenceViewModel
import kotlinx.coroutines.launch

class ProfilePageViewModel(private val preferenceViewModel: PreferenceViewModel) : ViewModel() {
    val accessToken = preferenceViewModel.accsessToken

    fun logout(token:String){
        viewModelScope.launch {
            preferenceViewModel.clearAccsessToken(token)
        }
    }
}