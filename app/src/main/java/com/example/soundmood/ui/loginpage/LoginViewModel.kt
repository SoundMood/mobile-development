package com.example.soundmood.ui.loginpage

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.soundmood.data.PreferenceViewModel
import com.example.soundmood.network.ApiConfig
import com.example.soundmood.network.UiState
import com.spotify.sdk.android.auth.AuthorizationResponse
import kotlinx.coroutines.launch

class LoginViewModel(private val preferenceViewModel: PreferenceViewModel) : ViewModel() {

    private val _uiState = MutableLiveData<UiState>()
    val uiState : LiveData<UiState> = _uiState

    private val _errorMessage = MutableLiveData<String>()
    val errorMessage : LiveData<String> = _errorMessage

    fun handleSpotifyResponse(response:AuthorizationResponse){
        _uiState.value = UiState.Loading
        when(response.type){
            AuthorizationResponse.Type.TOKEN ->{
                val accessToken = response.accessToken
                Log.d(TAG,"Access token received : $accessToken ")
                _uiState.value = UiState.Success
                saveAccessToken(accessToken)
                fetchAppToken(accessToken)
            }
            AuthorizationResponse.Type.ERROR->{
                Log.e(TAG,"Error : ${response.error}")
                _uiState.value = UiState.Error("Error : ${response.error}")
                _errorMessage.value = "Login error : ${response.error}"
            }
            else -> {
                Log.e(TAG,"Unexpected response type")
                _uiState.value = UiState.Error("Unexpected response type")
                _errorMessage.value = "Unexpected response type"
            }
        }
    }

    private fun saveAccessToken(accessToken:String){
        Log.d(TAG, "Saving access token: $accessToken")
        preferenceViewModel.saveAccsessToken(accessToken)
    }

    private fun fetchAppToken(accessToken: String){
        _uiState.value = UiState.Loading
        viewModelScope.launch {
            try {
                val response = ApiConfig.getSelfApi().getAppToken(accessToken)
                if(response.isSuccessful && response.body()!=null){
                    val appToken = response.body()?.appToken
                    Log.d(TAG,"App token received : $appToken")
                    preferenceViewModel.saveAppToken(appToken?:"")

                    preferenceViewModel.appToken.collect(){token->
                        if(!token.isNullOrEmpty()){
                            Log.d(TAG,"App token saved, emitting UiState.Success")
                            _uiState.value = UiState.Success
                        }
                    }
                }else{
                    Log.e(TAG,"Code : ${response.code()}- Message : ${response.message()}")
                    Log.e(TAG,"Error Body : ${response.errorBody()?.string()}")
                    _uiState.value = UiState.Error("$TAG : Failed to fetch app token")
                }
            }catch (e:Exception){
                Log.e(TAG,"Error : ${e.message}")
                _uiState.value = UiState.Error("Error : ${e.message}")
                _errorMessage.value = "Error : ${e.message}"
            }
        }
    }

    companion object{
        const val TAG = "LoginViewModel"
    }
}