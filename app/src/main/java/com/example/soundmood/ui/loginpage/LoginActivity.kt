package com.example.soundmood.ui.loginpage

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.asLiveData
import com.example.soundmood.data.PreferenceViewModel
import com.example.soundmood.databinding.ActivityLoginBinding
import com.example.soundmood.ui.ViewModelFactory
import com.example.soundmood.ui.fragment.MainActivity
import com.spotify.android.appremote.api.SpotifyAppRemote
import com.spotify.sdk.android.auth.AuthorizationClient
import com.spotify.sdk.android.auth.AuthorizationRequest
import com.spotify.sdk.android.auth.AuthorizationResponse
import com.spotify.sdk.android.auth.BuildConfig
import com.spotify.sdk.android.auth.LoginActivity.REQUEST_CODE


class LoginActivity : AppCompatActivity() {

    private lateinit var binding: ActivityLoginBinding
    private val requestCodes = 1337
    private val clientId = com.example.soundmood.BuildConfig.SPOTIFY_CLIENT_ID
    private val redirectUri = com.example.soundmood.BuildConfig.SPOTIFY_REDIRECT_URI

    private var spotifyAppRemote: SpotifyAppRemote? = null

    private val preferenceViewModel : PreferenceViewModel by viewModels{
        ViewModelFactory(applicationContext)
    }



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        preferenceViewModel.accsessToken.asLiveData().observe(this){token->
            if(token.isNullOrEmpty()){
                Log.d("LoginPage","Token is Null!")
            }else{
                // Ke main activity
                navigateMain()
            }
        }

        // Coba Auth ke akun Spotify
        binding.btnLogin.setOnClickListener {
            loginWithSpotify()
        }

    }

    private fun loginWithSpotify(){

        val accessToken = getAccessToken()
        if (accessToken != null) {
            startActivity(Intent(this, MainActivity::class.java))
        }else{
            val auth = AuthorizationRequest.Builder(
                clientId,
                AuthorizationResponse.Type.TOKEN,
                redirectUri
            )

            auth.setScopes(arrayOf(
                "user-read-private",
                "user-read-email",
                "user-read-currently-playing"
            ))

            val request = auth.build()

            try {
                Log.d("SpotifyAuth", "Attempting to open login activity")
                AuthorizationClient.openLoginActivity(this, requestCodes, request)
            } catch (e: Exception) {
                Log.e("SpotifyAuth", "Error opening login activity", e)
                Toast.makeText(this, "Login failed: ${e.message}", Toast.LENGTH_LONG).show()
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        Log.d("SpotifyAuth", "onActivityResult called. RequestCode: $requestCode")

        if (requestCode == requestCodes) {
            val response = AuthorizationClient.getResponse(resultCode, data)

            when (response.type) {
                AuthorizationResponse.Type.TOKEN -> {
                    val accessToken = response.accessToken
                    Log.d("SpotifyAuth", "Token received: $accessToken")

                    // Simpan access token pada datastore
                    saveAccessToken(accessToken)
                    Toast.makeText(this, "Login Successful!", Toast.LENGTH_SHORT).show()

                    // Ke main activity
                   navigateMain()
                }
                AuthorizationResponse.Type.ERROR -> {
                    Log.e("SpotifyAuth", "Error: ${response.error}")
                    Toast.makeText(this, "Login Error: ${response.error}", Toast.LENGTH_LONG).show()
                }
                else -> {
                    Log.e("SpotifyAuth", "Unexpected response type")
                    Toast.makeText(this, "Unexpected login response", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    private fun saveAccessToken(accessToken: String) {
        preferenceViewModel.saveAccsessToken(accessToken)
    }

    private fun getAccessToken():String?{
        var accessToken:String? =null
        preferenceViewModel.accsessToken.asLiveData().observe(this){token->
            accessToken = token
        }
        return accessToken
    }

    private fun navigateMain(){
        startActivity(Intent(this@LoginActivity,MainActivity::class.java))
        finish()
    }

}