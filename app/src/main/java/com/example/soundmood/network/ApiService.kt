package com.example.soundmood.network

import androidx.datastore.preferences.protobuf.Api
import com.example.soundmood.data.ApiResponse
import com.example.soundmood.data.RecommendationResponse
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.ResponseBody
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.POST
import retrofit2.http.Part
import retrofit2.http.Query

interface ApiService {
    @GET("recommendations")
    suspend fun getRecommendations(
        @Header("authorization") token:String,
        @Query("limit") limit : Int=10,
        @Query("seed_genres") seedGenres : String? = null,
        @Query("seed_tracks") seedTrackes : String? = null
    ): Response<RecommendationResponse>

    @POST("/predict")
    suspend fun uploadImage(
        @Part image:MultipartBody.Part
    ) : ResponseBody
}