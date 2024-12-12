package com.example.soundmood.ui.moodresultpage

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.soundmood.data.PlaylistResponse
import com.example.soundmood.data.PreferenceViewModel
import com.example.soundmood.network.ApiConfig
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.launch

class MoodResultViewModel(private val preferenceViewModel: PreferenceViewModel): ViewModel() {

    private val _playlistId = MutableLiveData<String>()
    val playListId : LiveData<String> = _playlistId

    private val _mood = MutableLiveData<String>()
    val mood : LiveData<String> = _mood

    private val _musicList = MutableLiveData<List<String>>()
    val musicList : LiveData<List<String>> = _musicList

    private val _loading = MutableLiveData<Boolean>()
    val loading : LiveData<Boolean> = _loading

    private val _errorMessage = MutableLiveData<String>()
    val errorMessage : LiveData<String> = _errorMessage

    suspend fun setPlaylistId(playlistId : String){
        _playlistId.value = playlistId
        fetchPlaylistDetails(playlistId)
    }

    private suspend fun fetchPlaylistDetails(playlistId: String) {
        _loading.value = true
        val appToken = preferenceViewModel.appToken.firstOrNull().toString()
        val accessToken = preferenceViewModel.accsessToken.firstOrNull().toString()
        Log.d(TAG, "App token: $appToken")
        Log.d(TAG,"Access token : $accessToken")
        Log.d(TAG, "Fetching playlist details for ID: $playlistId")

        viewModelScope.launch {
            try {
                var response = ApiConfig.getSelfApi().getPlaylistDetail("Bearer $appToken", playlistId)

                while (response.body()?.mood.isNullOrEmpty()){
                    response = ApiConfig.getSelfApi().getPlaylistDetail("Bearer $appToken", playlistId)
                    Log.d(TAG,"While...Fetching playlist detail")
                    kotlinx.coroutines.delay(2_000)
                }
                Log.d(TAG,"Finish fetching playlist detail")
                if (response.isSuccessful && response.body() != null) {
                    val songIds = response.body()?.songIds?.filterNotNull()?.map { it.split(":").last() } ?: emptyList()
                    _loading.value= false
                    _musicList.value = songIds
                    _mood.value = response.body()?.mood.toString()
                    Log.d(TAG,"Mood ${response.body()?.mood}")
                    Log.d(TAG,"Songs ${musicList.value}")

                } else {
                    _loading.value = false
                    _errorMessage.value = "API error: ${response.message()}"
                    Log.e(TAG, "API Response Error: ${response.message()}")
                    Log.e(TAG, "Raw response: ${response.raw()}")
                }
            } catch (e: Exception) {
                _loading.value = false
                _errorMessage.value = "Error fetching playlist details: ${e.message}"
                Log.e(TAG, "Exception: ${e.message}")
            }
        }
    }


    companion object{
        const val TAG = "MoodResultViewModel"
    }
}