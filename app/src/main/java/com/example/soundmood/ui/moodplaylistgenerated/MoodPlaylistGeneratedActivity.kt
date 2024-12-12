package com.example.soundmood.ui.moodplaylistgenerated

import android.os.Bundle
import android.util.Log
import android.widget.LinearLayout
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.soundmood.R
import com.example.soundmood.databinding.ActivityMoodPlaylistGeneratedBinding
import com.example.soundmood.ui.ViewModelFactory
import com.example.soundmood.ui.moodresultpage.MoodResultActivity.Companion.EXTRA_MUSIC_LIST
import kotlinx.coroutines.launch

class MoodPlaylistGeneratedActivity : AppCompatActivity() {

    private lateinit var binding : ActivityMoodPlaylistGeneratedBinding

    private val moodPlaylistGeneratedViewModel:MoodPlaylistGeneratedViewModel by viewModels{
        ViewModelFactory(applicationContext)
    }
    companion object{
        const val EXTRA_MUSIC_LIST = "extra_music_list"
        const val TAG = "MoodPlaylistGeneratedActivity"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityMoodPlaylistGeneratedBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.rvTrack.layoutManager = LinearLayoutManager(this)
        val musicList = intent.getStringArrayListExtra(EXTRA_MUSIC_LIST)
        if(musicList!=null){
            lifecycleScope.launch {
                fetchMusic(musicList)
            }
            Toast.makeText(this@MoodPlaylistGeneratedActivity,"$musicList",Toast.LENGTH_SHORT).show()
        }else{
            Log.d(TAG,"Music list is null")
        }

        observeTracks()
    }

    private fun observeTracks() {
        moodPlaylistGeneratedViewModel.tracks.observe(this){trackList->
            val adapter = RecyclerViewAdapter(trackList)
            binding.rvTrack.adapter = adapter
        }
    }

    private suspend fun fetchMusic(musicList: ArrayList<String>) {
        moodPlaylistGeneratedViewModel.setMusicList(musicList)
    }
}