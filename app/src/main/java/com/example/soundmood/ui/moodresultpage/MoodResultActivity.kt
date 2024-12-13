package com.example.soundmood.ui.moodresultpage

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
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
        val userId = intent.getStringExtra("USER_ID")
        if(playlistId!=null){
            lifecycleScope.launch {
                fetchPlaylistDetails(playlistId)
            }
        }else{
            Log.d(TAG,"There is no Playlist ID")
        }
        moodResultViewModel.loading.observe(this){loading->
            if(loading){
                binding.ivLoad.visibility = View.VISIBLE
                binding.progressBar.visibility = View.VISIBLE
            }else{
                binding.ivLoad.visibility = View.GONE
                binding.progressBar.visibility = View.GONE
            }
        }
        observeMood()

        binding.btnGenerate.setOnClickListener {
            val musicList = moodResultViewModel.musicList.value
            if (musicList!=null){
                val intent = Intent(this,MoodPlaylistGeneratedActivity::class.java)
                intent.putStringArrayListExtra(EXTRA_MUSIC_LIST, ArrayList(musicList))
                intent.putExtra("USER_ID",userId)

                startActivity(intent)
            }else{
                Log.d(TAG,"Music list empty.")
            }
        }
    }

    private fun observeMood() {
        moodResultViewModel.mood.observe(this) { mood ->
            if (mood != null) {
                when(mood){
                    "angry"-> {
                        binding.imgMoodResult.setImageResource(R.drawable.angry)
                        binding.tvmoodresult.text = mood
                    }
                    "happy"-> {
                        binding.imgMoodResult.setImageResource(R.drawable.happy)
                        binding.tvmoodresult.text = mood
                    }
                    "neutral"-> {
                        binding.imgMoodResult.setImageResource(R.drawable.neutral)
                        binding.tvmoodresult.text = mood
                    }
                    "sad"-> {
                        binding.imgMoodResult.setImageResource(R.drawable.sad)
                        binding.tvmoodresult.text = mood
                    }
                    "surprised"-> {
                        binding.imgMoodResult.setImageResource(R.drawable.surprised)
                        binding.tvmoodresult.text = mood
                    }
                }
                Toast.makeText(this@MoodResultActivity,"Mood : $mood",Toast.LENGTH_SHORT).show()
            } else {
                Log.d(TAG, "Mood is null or not set yet.")
            }
        }
    }

    private fun navigateMoodPlaylistGenerated() {

    }


    private suspend fun fetchPlaylistDetails(playlistId: String) {
        moodResultViewModel.setPlaylistId(playlistId)
    }


}