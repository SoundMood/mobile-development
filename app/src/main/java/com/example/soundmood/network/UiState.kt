package com.example.soundmood.network

sealed class UiState {
    object Success : UiState()
    data class Error(val message:String) : UiState()
    object Loading : UiState()
}