package com.example.soundmood.ui.fragment

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.findNavController
import androidx.navigation.ui.setupWithNavController
import com.example.soundmood.R
import com.example.soundmood.data.PreferenceRepository
import com.example.soundmood.data.PreferenceViewModel
import com.example.soundmood.databinding.ActivityMainBinding
import com.example.soundmood.ui.ViewModelFactory
import com.example.soundmood.ui.captureimagepage.CaptureImagePage
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.spotify.android.appremote.api.SpotifyAppRemote
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity() {
    private var _binding : ActivityMainBinding?=null
    private val binding get(): ActivityMainBinding = _binding!!

    private var spotifyAppRemote: SpotifyAppRemote? = null

    private val preferenceViewModel: PreferenceViewModel by viewModels {
        ViewModelFactory(this)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                preferenceViewModel.accsessToken.collect { accessToken ->
                    if (accessToken != null) {
                        Log.d("SpotifyAuth", "Access token retrieved: $accessToken")
                    } else {
                        Log.e("SpotifyAuth", "No access token found!")
                    }
                }
            }
        }


        val navView : BottomNavigationView = binding.navigationView
        val navController = findNavController(R.id.navigation_main_activity_host)



        navView.setupWithNavController(navController)
        binding.fabCaptureImage.setOnClickListener {
            startActivity(Intent(this@MainActivity, CaptureImagePage::class.java))
        }
    }





}