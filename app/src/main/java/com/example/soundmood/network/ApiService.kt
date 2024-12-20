package com.example.soundmood.network

import com.example.soundmood.data.AddTracksRequest
import com.example.soundmood.data.AddTracksResponse
import com.example.soundmood.data.PlaylistRequest
import com.example.soundmood.data.AlbumResponse
import com.example.soundmood.data.PlaylistApiResponse
import com.example.soundmood.data.PlaylistResponse
import com.example.soundmood.data.PredictResponse
import com.example.soundmood.data.TokenResponse
import com.example.soundmood.data.TrackResponse
import com.example.soundmood.data.UserProfile
import okhttp3.MultipartBody
import retrofit2.Response
import retrofit2.http.Body
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

    // Spotify API Call for Getting Playlist from Playlist ID
    @GET("playlists/{playlist_id}")
    suspend fun getPlaylist(
        @Header("Authorization") token:String,
        @Path("playlist_id") playlistId: String
    ): Response<PlaylistApiResponse>

    // Spotify API Call for Several Album
    @GET("albums")
    suspend fun getSeveralAlbums(
        @Header("Authorization") appToken: String,
        @Query("ids") ids:String,
        @Query("market") market:String?=null
    ): Response<AlbumResponse>


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

    @POST("users/{user_id}/playlists")
    suspend fun createPlaylist(
        @Header("Authorization") appToken: String,
        @Path("user_id") userId: String?,
        @Body playlistRequest: PlaylistRequest
    ): Response<PlaylistResponse>

    @POST("playlists/{playlist_id}/tracks")
    suspend fun addTracksToPlaylist(
        @Header("Authorization") appToken: String,
        @Path("playlist_id") playlistId: String,
        @Body tracks: AddTracksRequest
    ): Response<AddTracksResponse>

}