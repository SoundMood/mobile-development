package com.example.soundmood.ui.fragment.historypage

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.soundmood.data.PreferenceViewModel

class HistoryPageViewModel(private val preferenceViewModel: PreferenceViewModel): ViewModel() {
    private val _loading = MutableLiveData<Boolean>()
    val loading :LiveData<Boolean> = _loading

}