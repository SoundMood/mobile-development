package com.example.soundmood.network

import com.example.soundmood.data.PredictResponse
import com.example.soundmood.data.TokenResponse
import com.example.soundmood.data.UserProfile
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.ResponseBody
import retrofit2.Response
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part

interface ApiService {
    @GET("me")
    suspend fun getCurrentUser(
        @Header("Authorization") token:String,
    ): Response<UserProfile>

    @Multipart
    @POST("me/predict")
    suspend fun generatePlaylist(
        @Header("Authorization") appToken: String,
        @Part("access_token") accessToken: String,
        @Part image:MultipartBody.Part
    ) : Response<PredictResponse>

    @FormUrlEncoded
    @POST("auth/token")
    suspend fun getAppToken(
        @Field("access_token") accessToken:String
    ):Response<TokenResponse>



}