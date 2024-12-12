package com.example.soundmood.ui.moodplaylistgenerated

import android.os.Bundle
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.soundmood.R
import com.example.soundmood.databinding.ActivityMoodPlaylistGeneratedBinding
import com.example.soundmood.ui.moodresultpage.MoodResultActivity.Companion.EXTRA_MUSIC_LIST

class MoodPlaylistGeneratedActivity : AppCompatActivity() {

    private lateinit var binding : ActivityMoodPlaylistGeneratedBinding

    companion object{
        const val EXTRA_MUSIC_LIST = "extra_music_list"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityMoodPlaylistGeneratedBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val musicList = intent.getStringArrayListExtra(EXTRA_MUSIC_LIST)
        Toast.makeText(this@MoodPlaylistGeneratedActivity,"$musicList",Toast.LENGTH_SHORT).show()
    }
}