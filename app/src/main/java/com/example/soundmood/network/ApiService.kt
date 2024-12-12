package com.example.soundmood.network

import com.example.soundmood.data.PlaylistResponse
import com.example.soundmood.data.PredictResponse
import com.example.soundmood.data.TokenResponse
import com.example.soundmood.data.TrackResponse
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
import retrofit2.http.Path
import retrofit2.http.Query

interface ApiService {

    // Spotify API Call for Authorization
    @GET("me")
    suspend fun getCurrentUser(
        @Header("Authorization") token: String,
    ): Response<UserProfile>

    // Spotify API Call for Getting Tracks
    @GET("tracks?id=")
    suspend fun getTracks(
        @Header("Authorization") token: String,
        @Query("ids") ids:String
    ): Response<TrackResponse>

    // SoundMood Api Call for Getting Playlist Detail
    @GET("me/playlists/{playlist_id}")
    suspend fun getPlaylistDetail(
        @Header("Authorization") appToken: String,
        @Path("playlist_id") playlistId: String
    ): Response<PlaylistResponse>

    // SoundMood API Call for Posting Face for Prediction
    @Multipart
    @POST("me/predict")
    suspend fun generatePlaylist(
        @Header("Authorization") appToken: String,
        @Part image: MultipartBody.Part,
        @Part accessToken: MultipartBody.Part
    ): Response<PredictResponse>


    // SoundMood Api Call for Posting Access Token (Spotify) and Getting App Token (SoundMood)
    @FormUrlEncoded
    @POST("auth/token")
    suspend fun getAppToken(
        @Field("access_token") accessToken: String
    ): Response<TokenResponse>

}