package com.example.soundmood.ui.loginpage

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.soundmood.databinding.ActivityLoginBinding
import com.example.soundmood.ui.fragment.MainActivity
import com.spotify.android.appremote.api.SpotifyAppRemote
import com.spotify.sdk.android.auth.AuthorizationClient
import com.spotify.sdk.android.auth.AuthorizationRequest
import com.spotify.sdk.android.auth.AuthorizationResponse
import com.spotify.sdk.android.auth.LoginActivity.REQUEST_CODE


class LoginActivity : AppCompatActivity() {

    private lateinit var binding: ActivityLoginBinding
    private val requestCodes = 1337
    private val clientId = "fc6d180eb8ba48e08a914e29d3c812ea"
    private val redirectUri = "com.example.authorizationtest://callback"
    private var spotifyAppRemote: SpotifyAppRemote? = null



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Coba Auth ke Spotify
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
                    saveToken(accessToken)
                    Log.d("SpotifyAuth", "Token received: $accessToken")
                    Toast.makeText(this, "Login Successful!", Toast.LENGTH_SHORT).show()
                    startActivity(Intent(this@LoginActivity,MainActivity::class.java))
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

    private fun saveToken(accessToken: String) {
        val sharedPreferences = getSharedPreferences("spotify_prefs", MODE_PRIVATE)
        val editor = sharedPreferences.edit()
        editor.putString("access_token", accessToken)
        editor.apply()
    }

    private fun getAccessToken(): String? {
        val sharedPreferences = getSharedPreferences("spotify_prefs", MODE_PRIVATE)
        return sharedPreferences.getString("access_token", null)
    }





}