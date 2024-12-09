package com.example.soundmood.ui.fragment.homepage

import android.util.Log
import android.widget.Toast
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.soundmood.data.PreferenceViewModel
import com.example.soundmood.data.UserProfile
import com.example.soundmood.network.ApiConfig
import kotlinx.coroutines.launch

class HomePageViewModel(private val preferenceViewModel: PreferenceViewModel) : ViewModel() {



    private val _error = MutableLiveData<String>()
    val error:LiveData<String> get() = _error

    private val _userName = MutableLiveData<String>()
    val userName:LiveData<String> get() = _userName

    private val _userImageProfile = MutableLiveData<String>()
    val userImageProfile:LiveData<String> get() = _userImageProfile

    private var accessToken:String?=null

    init{
        viewModelScope.launch {
            preferenceViewModel.accsessToken.collect{token->
                accessToken = token
                getUserProfile()
            }
        }

    }

    fun getUserProfile(){

        if(accessToken.isNullOrEmpty()){
            _error.value = "Access token is not available!"
            return
        }

        viewModelScope.launch {
            try {
                val authHeader = "Bearer $accessToken"
                val response = ApiConfig.getApiService().getCurrentUser(authHeader)

                if (response.isSuccessful && response.body() != null) {
                    val userProfile = response.body()!!
                    _userName.value = userProfile.displayName ?: "Unknown User"
                    _userImageProfile.value = userProfile.images?.firstOrNull()?.url ?: ""
                    Log.d("HomePageViewModel","UserName : $userName")
                }
            } catch (e : Exception){
                _error.value = "Exception : ${e.message}"
            }
        }
    }


}