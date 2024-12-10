package com.example.soundmood.network

import com.example.soundmood.data.UserProfile
import okhttp3.MultipartBody
import okhttp3.ResponseBody
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.POST
import retrofit2.http.Part

interface ApiService {
    @GET("me")
    suspend fun getCurrentUser(
        @Header("Authorization") token:String,
    ): Response<UserProfile>

    @POST("/predict")
    suspend fun uploadImage(
        @Part image:MultipartBody.Part
    ) : ResponseBody
}