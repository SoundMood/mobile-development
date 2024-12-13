package com.example.soundmood.ui.loginpage

import android.animation.Animator
import android.animation.ObjectAnimator
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.asLiveData
import com.example.soundmood.data.PreferenceViewModel
import com.example.soundmood.databinding.ActivityLoginBinding
import com.example.soundmood.network.UiState
import com.example.soundmood.ui.ViewModelFactory
import com.example.soundmood.ui.fragment.MainActivity
import com.spotify.android.appremote.api.SpotifyAppRemote
import com.spotify.sdk.android.auth.AuthorizationClient
import com.spotify.sdk.android.auth.AuthorizationRequest
import com.spotify.sdk.android.auth.AuthorizationResponse


class LoginActivity : AppCompatActivity() {

    private lateinit var binding: ActivityLoginBinding
    private var spotifyAppRemote: SpotifyAppRemote? = null
    

    // Companion Object
    companion object{
        const val TAG = "TAG"
        const val REQUESTCODE = 1337
        const val CLIENTID = "65498186928145118afa24d6019af2af"
        const val REDIRECTURI = "com.example.authorizationtest://callback"
    }

    // loginViewModel inisialisasi
    private val viewModel : LoginViewModel by viewModels {
        ViewModelFactory(applicationContext)
    }

    // PreferenceViewModel inisialisasi
    private val preferenceViewModel : PreferenceViewModel by viewModels {
        ViewModelFactory(applicationContext)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)



        // Observe access token dari PreferenceViewModel
        preferenceViewModel.accsessToken.asLiveData().observe(this){token->
            if(!token.isNullOrEmpty()) {
                navigateMain()
            }else{
                // Handle tombol login
                binding.btnLogin.setOnClickListener {
                    startSpotifyLogin()

                }
            }
        }
        // Untuk observe state LoginViewModel
        observeViewModel()
    }





    private fun observeViewModel(){
        Log.d("TAG", "Observe View Model")
        viewModel.uiState.observe(this){state->
            when(state){
                is UiState.Loading ->
                {
                    Log.d("TAG","Loading Occurred")
//                    showToast("Loading")
                }
                is UiState.Success -> {
                    Log.d("TAG","Navigate to Main")
                    navigateMain()
                }
                is UiState.Error -> {
                    Log.e("TAG","Error Occurred")
                }
            }
        }
    }


    // Fungsi handle tombol login
    private fun startSpotifyLogin(){
        Log.d("TAG", "Start Spotify Login")
        val auth = AuthorizationRequest.Builder(
            CLIENTID,
            AuthorizationResponse.Type.TOKEN,
            REDIRECTURI
        ).setScopes(arrayOf("user-read-playback-state",
            "user-modify-playback-state",
            "user-read-currently-playing",
            "app-remote-control",
            "playlist-read-private",
            "playlist-read-collaborative",
            "playlist-modify-private",
            "playlist-modify-public",
            "user-read-playback-position",
            "user-read-recently-played",
            "user-top-read",
            "user-library-read",
            "user-read-email"))
            .build()
        AuthorizationClient.openLoginActivity(this, REQUESTCODE,auth)
    }


    @Deprecated("This method has been deprecated in favor of using the Activity Result API\n      which brings increased type safety via an {@link ActivityResultContract} and the prebuilt\n      contracts for common intents available in\n      {@link androidx.activity.result.contract.ActivityResultContracts}, provides hooks for\n      testing, and allow receiving results in separate, testable classes independent from your\n      activity. Use\n      {@link #registerForActivityResult(ActivityResultContract, ActivityResultCallback)}\n      with the appropriate {@link ActivityResultContract} and handling the result in the\n      {@link ActivityResultCallback#onActivityResult(Object) callback}.")
    override fun onActivityResult(
        requestCode: Int,
        resultCode: Int,
        data: Intent?
    ) {
        super.onActivityResult(requestCode, resultCode, data)
        Log.d("TAG", "onActivity Result")
        if(requestCode== REQUESTCODE){
            val response = AuthorizationClient.getResponse(resultCode,data)
            Log.d(TAG,"OnActivityResult Occured and Try Handle Response")
            viewModel.handleSpotifyResponse(response)
        }
    }

    // Fungsi intent ke MainActivity (Homepage)
    private fun navigateMain(){
        Log.d("TAG", "Navigate Main")
        startActivity(Intent(this@LoginActivity,MainActivity::class.java))
        finish()
    }
    
}