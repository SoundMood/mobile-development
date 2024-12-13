package com.example.soundmood.ui.moodplaylistgenerated

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.soundmood.data.TracksItem
import com.example.soundmood.databinding.CardviewListBinding

class RecyclerViewAdapter(private val tracks:List<TracksItem>):RecyclerView.Adapter<RecyclerViewAdapter.RecyclerViewHolder>() {
    class RecyclerViewHolder(private val binding : CardviewListBinding):RecyclerView.ViewHolder(binding.root) {
        fun bind(track:TracksItem){
            binding.apply {
                tvTrackName.text = track.name
                tvArtistName.text = track.artists?.joinToString(",") { it?.name?:"" }

                val imageUrl = track.album?.images?.firstOrNull()?.url
                Glide.with(itemView.context)
                    .load(imageUrl)
                    .into(ivAlbumCover)

                btnPlay.setOnClickListener{
                    Log.d("MoodPlaylistGeneratedActivity","Music Played")
                }
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerViewHolder {
        val binding = CardviewListBinding.inflate(LayoutInflater.from(parent.context),parent,false)
        return RecyclerViewHolder(binding)
    }

    override fun onBindViewHolder(holder: RecyclerViewHolder, position: Int) {
        holder.bind(tracks[position])
    }

    override fun getItemCount(): Int = tracks.size


}

