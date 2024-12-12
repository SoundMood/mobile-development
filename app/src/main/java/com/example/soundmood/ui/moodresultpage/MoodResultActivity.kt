package com.example.soundmood.ui.moodresultpage

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.lifecycleScope
import com.example.soundmood.R
import com.example.soundmood.databinding.ActivityMoodResultBinding
import com.example.soundmood.ui.ViewModelFactory
import com.example.soundmood.ui.moodplaylistgenerated.MoodPlaylistGeneratedActivity
import kotlinx.coroutines.launch
import java.util.ArrayList

class MoodResultActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMoodResultBinding
    private val moodResultViewModel : MoodResultViewModel by viewModels {
        ViewModelFactory(applicationContext)
    }

    companion object{
        const val TAG = "MoodResultActivity"
        const val EXTRA_MUSIC_LIST = "extra_music_list"
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMoodResultBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val playlistId = intent.getStringExtra("PLAYLIST_ID")
        if(playlistId!=null){
            lifecycleScope.launch {
                fetchPlaylistDetails(playlistId)
            }
        }else{
            Log.d(TAG,"There is no Playlist ID")
        }
        moodResultViewModel.loading.observe(this){loading->
            if(loading){
                binding.progressBar.visibility = android.view.View.VISIBLE
            }else{
                binding.progressBar.visibility = android.view.View.GONE
            }
        }

        observeMood()

        binding.btnGenerate.setOnClickListener {
            navigateMoodPlaylistGenerated()
        }
    }

    private fun observeMood() {
        moodResultViewModel.mood.observe(this) { mood ->
            if (mood != null) {
                when(mood){
                    "angry"->binding.imgMoodResult.setImageResource(R.drawable.angry)
                    "happy"->binding.imgMoodResult.setImageResource(R.drawable.happy)
                    "neutral"->binding.imgMoodResult.setImageResource(R.drawable.neutral)
                    "sad"->binding.imgMoodResult.setImageResource(R.drawable.sad)
                    "surprised"->binding.imgMoodResult.setImageResource(R.drawable.surprised)
                }
                Toast.makeText(this@MoodResultActivity,"Mood : $mood",Toast.LENGTH_SHORT).show()
            } else {
                Log.d(TAG, "Mood is null or not set yet.")
            }
        }
    }

    private fun navigateMoodPlaylistGenerated() {
        val musicList = moodResultViewModel.musicList.value
        if (musicList!=null){
            val intent = Intent(this,MoodPlaylistGeneratedActivity::class.java)
            intent.putStringArrayListExtra(EXTRA_MUSIC_LIST, ArrayList(musicList))
            startActivity(intent)
        }else{
            Log.d(TAG,"Music list empty.")
        }
    }


    private suspend fun fetchPlaylistDetails(playlistId: String) {
        moodResultViewModel.setPlaylistId(playlistId)
    }


}