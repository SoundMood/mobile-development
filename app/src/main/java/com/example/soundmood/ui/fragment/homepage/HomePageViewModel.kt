package com.example.soundmood.ui.fragment.homepage

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.soundmood.data.PreferenceViewModel
import com.example.soundmood.data.Track
import com.example.soundmood.network.ApiConfig
import kotlinx.coroutines.launch

class HomePageViewModel(private val preferenceViewModel: PreferenceViewModel) : ViewModel() {

    private val _recommendations = MutableLiveData<List<Track>>()
    val recommendations : LiveData<List<Track>> get() = _recommendations

    private val _error = MutableLiveData<String>()
    val error:LiveData<String> get() = _error

    private var accessToken:String?=null

    init{
        viewModelScope.launch {
            preferenceViewModel.accsessToken.collect{token->
                accessToken = token
            }
        }
    }

    fun getRecommendations(){

        if(accessToken.isNullOrEmpty()){
            _error.value = "Access token is not available!"
            return
        }

        viewModelScope.launch {
            try {
                val response = ApiConfig.getApiService().getRecommendations(
                    token = accessToken?:""
                )
                if (response.isSuccessful){
                    _recommendations.value = response.body()?.tracks?.items
                }else{
                    _error.value = "Error : ${response.message()}"
                }
            } catch (e : Exception){
                _error.value = "Exception : ${e.message}"
            }
        }
    }


}