package com.example.soundmood.ui.fragment.homepage

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.soundmood.R
import com.example.soundmood.data.Track
import com.example.soundmood.data.Tracks
import com.example.soundmood.databinding.ItemTrackBinding

class RecyclerViewAdapter(private val tracks : List<Track>): RecyclerView.Adapter<RecyclerViewAdapter.RecylcerViewHolder>() {
    class RecylcerViewHolder (private val binding : ItemTrackBinding): RecyclerView.ViewHolder(binding.root){
        fun bind(track:Track){
            // Menampilkan nama track
            binding.tvAlbumTitle.text = track.name

            // Menampilkan nama artis (menggabungkan jika ada lebih dari satu artis)
            val artistNames = track.artists.joinToString(", ") { it.name }
            binding.tvAlbumDuration.text = artistNames

            // Menampilkan nama album
            binding.tvAlbumTitle.text = track.album.name

            // Menggunakan Glide untuk memuat gambar album
            val albumImageUrl = track.album.images.firstOrNull()?.url
            if (!albumImageUrl.isNullOrEmpty()) {
                Glide.with(binding.ivAlbumCover)
                    .load(albumImageUrl)
                    .into(binding.ivAlbumCover)
            } else {
                // Menangani kasus jika tidak ada gambar
                binding.ivAlbumCover.setImageResource(R.drawable.illustration_login)  // Gambar placeholder
            }

        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecylcerViewHolder {
        val binding = ItemTrackBinding.inflate(LayoutInflater.from(parent.context),parent,false)
        return RecylcerViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return tracks.size
    }

    override fun onBindViewHolder(holder: RecylcerViewHolder, position: Int) {
        val track = tracks[position]
        holder.bind(track)
    }


}