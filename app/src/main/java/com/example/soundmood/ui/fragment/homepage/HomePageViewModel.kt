package com.example.soundmood.ui.fragment.homepage

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.soundmood.data.PlaylistApiResponse
import com.example.soundmood.data.PreferenceViewModel
import com.example.soundmood.network.ApiConfig
import com.example.soundmood.util.Utility.Companion.albumIds
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.launch

class HomePageViewModel(private val preferenceViewModel: PreferenceViewModel) : ViewModel() {

    private val _recommendationAlbum = MutableLiveData<List<PlaylistApiResponse>>()
    val recommendationAlbum : LiveData<List<PlaylistApiResponse>> = _recommendationAlbum

    private val _error = MutableLiveData<String>()
    val error:LiveData<String> get() = _error

    private val _userName = MutableLiveData<String>()
    val userName:LiveData<String> get() = _userName

    private val _userId = MutableLiveData<String>()
    val userId:LiveData<String> get() = _userId

    private val _userImageProfile = MutableLiveData<Any>()
    val userImageProfile:LiveData<Any> get() = _userImageProfile

    private var accessToken: String? = null
    private var appToken : String? = null

    private var isDataLoaded : Boolean = false

    private var isAppTokenBeingFetched = false
    private var isUserProfileBeingFetched = false

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading : LiveData<Boolean> get() = _isLoading

    init{
        loadInitialData()
    }


    private fun loadInitialData() {
        viewModelScope.launch {
            accessToken = preferenceViewModel.accsessToken.firstOrNull()
            appToken = preferenceViewModel.getAppToken()

            if (accessToken.isNullOrEmpty()) {
                _error.value = "Access token is missing."
                return@launch
            }

            if (appToken.isNullOrEmpty()) {
                getAppToken()
            } else {
                getUserProfile()
            }
        }
    }

    fun getSeveralAlbum(){
        if (isDataLoaded) return
        if(accessToken.isNullOrEmpty()){
            _error.value = "Access token is not available!"
            return
        }

        if(isUserProfileBeingFetched) return
        isAppTokenBeingFetched = true

        viewModelScope.launch {
            try {
                _isLoading.value = true
                val albumIdsToFetch = albumIds.shuffled().take(6).joinToString(",")
                val response = ApiConfig.getApiService().getSeveralAlbums("Bearer $accessToken",albumIdsToFetch)
                if(response.isSuccessful && response.body()!=null){
                    val tracks = response.body()?.albums?.map { albumId ->
                        PlaylistApiResponse(
                            name = albumId?.name,
                            images = albumId?.images
                        )
                    } ?: emptyList()

                    _recommendationAlbum.value = tracks
                }else{
                    Log.e("HomePageFragment","API Error ${response.raw()}")
                }
            }catch (e:Exception){
                Log.e("HomePageFragment","Exception ${e.message}")
            }finally {
                _isLoading.value = false
            }
        }
    }

    // Get atau Refresh App Token Baru!
    fun getAppToken(){
        if(isDataLoaded) return
        if(accessToken.isNullOrEmpty()){
            _error.value = "Access token is not available!"
            return
        }

        if(isAppTokenBeingFetched) return
        isAppTokenBeingFetched = true

        viewModelScope.launch {
            try {
                val response = ApiConfig.getSelfApi().getAppToken(accessToken.toString())
                if (response.isSuccessful && response.body() != null) {
                    val newAppToken = response.body()?.appToken

                    if(newAppToken != null && newAppToken != appToken) {

                        appToken = newAppToken

                        preferenceViewModel.saveAppToken(newAppToken.toString())
                        Log.d("HomePageViewModel", "App Token : ${response.body()?.appToken}")
                        getUserProfile()
                    }
                }
            } catch (e : Exception){
                Log.d("HomePageViewModel","App Token : ${e.message}")
                _error.value = "Exception : ${e.message}"
            } finally {
                isAppTokenBeingFetched = false
            }
        }
    }

    fun updateUserProfile(newName: String, newImage: String) {
        viewModelScope.launch {
            try {
                val response = ApiConfig.getApiService().getCurrentUser("Bearer $accessToken")
                if (response.isSuccessful) {
                    isDataLoaded = false
                    getUserProfile()
                }
            } catch (e: Exception) {
                Log.e("HomePageViewModel", "Error updating profile: ${e.message}")
                _error.value = "Failed to update profile"
            }
        }
    }

    fun getUserProfile(){
        if(isDataLoaded) return
        if(accessToken.isNullOrEmpty()){
            _error.value = "Access token is not available!"
            return
        }

        if(isUserProfileBeingFetched) return
        isAppTokenBeingFetched = true

        viewModelScope.launch {
            try {
                _isLoading.value = true
                val authHeader = "Bearer $accessToken"
                val response = ApiConfig.getApiService().getCurrentUser(authHeader)

                if (response.isSuccessful && response.body() != null) {
                    _isLoading.value = false
                    val userProfile = response.body()!!
                    _userName.value = userProfile.displayName ?: "Unknown User"
                    _userId.value = userProfile.id ?: "No ID"
                    _userImageProfile.value = userProfile.images?.firstOrNull()?.url ?: ""
                    Log.d("HomePageViewModel","UserName : $_userName")
                    isDataLoaded = true
                }
            } catch (e : Exception){
                _isLoading.value = false
                Log.e("HomePageViewModel","${e.message}")
                _error.value = "Exception : ${e.message}"
            } finally {
                _isLoading.value = false
                isUserProfileBeingFetched = false
            }
        }
    }
}