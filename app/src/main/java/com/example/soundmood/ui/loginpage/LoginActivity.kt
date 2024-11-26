package com.example.soundmood.ui.loginpage

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.soundmood.databinding.ActivityLoginBinding
import com.example.soundmood.ui.fragment.MainActivity
import com.spotify.android.appremote.api.SpotifyAppRemote


class LoginActivity : AppCompatActivity() {

    private lateinit var binding: ActivityLoginBinding
    private val requestCode = 1337
    private val clientId = "fc6d180eb8ba48e08a914e29d3c812ea"
    private val redirectUri = "https://youtube.com"
    private var spotifyAppRemote: SpotifyAppRemote? = null



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.btnLogin.setOnClickListener {
            startActivity(Intent(this@LoginActivity, MainActivity::class.java))
        }
    }





}