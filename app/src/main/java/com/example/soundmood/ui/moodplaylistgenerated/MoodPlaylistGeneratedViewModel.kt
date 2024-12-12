package com.example.soundmood.ui.moodplaylistgenerated

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.soundmood.data.PreferenceViewModel
import com.example.soundmood.data.TracksItem
import com.example.soundmood.network.ApiConfig
import kotlinx.coroutines.flow.firstOrNull
import okhttp3.internal.notify

class MoodPlaylistGeneratedViewModel(private val preferenceViewModel: PreferenceViewModel) : ViewModel() {
    private val _musicList = MutableLiveData<String>()
    val musicList : LiveData<String> = _musicList

    private val _tracks = MutableLiveData<List<TracksItem>>()
    val tracks : LiveData<List<TracksItem>> = _tracks

    private val _loading = MutableLiveData<Boolean>()
    val loading : LiveData<Boolean> = _loading

    companion object{
        const val TAG = "MoodPlaylistGeneratedViewModel"
    }

    suspend fun setMusicList(musicList : List<String>){
        val musicListString = musicList.joinToString(",")
        _musicList.value = musicListString
        fetchMusicDetail(musicListString)
    }

    private suspend fun fetchMusicDetail(musicListString: String) {
        val accessToken = preferenceViewModel.accsessToken.firstOrNull().toString()
        try {
            _loading.value = true
            val response = ApiConfig.getApiService().getTracks("Bearer $accessToken",musicListString)
            if(response.isSuccessful && response.body() !=null){
                _loading.value = false
                val trackList = response.body()?.tracks?.filterNotNull()?: emptyList()
                Log.d(TAG,"$trackList")
                _tracks.value = trackList
            }

        }catch (e:Exception){
            _loading.value = false
            Log.e(TAG,"Error message : ${e.message}")
        }
    }

}