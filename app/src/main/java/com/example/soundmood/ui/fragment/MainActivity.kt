package com.example.soundmood.ui.fragment

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.findNavController
import androidx.navigation.ui.setupWithNavController
import com.example.soundmood.R
import com.example.soundmood.databinding.ActivityMainBinding
import com.example.soundmood.ui.captureimagepage.CaptureImagePage
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.spotify.android.appremote.api.SpotifyAppRemote

class MainActivity : AppCompatActivity() {
    private var _binding : ActivityMainBinding?=null
    private val binding get(): ActivityMainBinding = _binding!!

    private var spotifyAppRemote: SpotifyAppRemote? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val accessToken = getAccessToken()
        if (accessToken != null) {
            Log.d("SpotifyAuth", "Access token retrieved: $accessToken")
        } else {
            Log.e("SpotifyAuth", "No access token found!")
        }


        val navView : BottomNavigationView = binding.navigationView
        val navController = findNavController(R.id.navigation_main_activity_host)


        navView.setupWithNavController(navController)
        binding.fabCaptureImage.setOnClickListener {
            startActivity(Intent(this@MainActivity, CaptureImagePage::class.java))
        }
    }


    // Fungsi untuk mengambil token dari SharedPreferences
    private fun getAccessToken(): String? {
        val sharedPreferences = getSharedPreferences("spotify_prefs", MODE_PRIVATE)
        return sharedPreferences.getString("access_token", null)
    }


}