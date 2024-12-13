package com.example.soundmood.ui.fragment.homepage

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.soundmood.data.PlaylistApiResponse
import com.example.soundmood.databinding.ItemTrackBinding

class HomePageAdapter(private val tracks: MutableList<PlaylistApiResponse>): RecyclerView.Adapter<HomePageAdapter.HomePageHolder>() {
    class HomePageHolder(private val binding : ItemTrackBinding):RecyclerView.ViewHolder(binding.root) {
        fun bind(tracks:PlaylistApiResponse){
            binding.tvAlbumTitle.text = tracks.name
            Glide.with(binding.root.context)
                .load(tracks.images?.firstOrNull()?.url)
                .into(binding.ivAlbumCover)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HomePageHolder {
        val binding = ItemTrackBinding.inflate(LayoutInflater.from(parent.context),parent,false)
        return HomePageHolder(binding)
    }

    override fun getItemCount(): Int = tracks.size

    override fun onBindViewHolder(holder: HomePageHolder, position: Int) {
        holder.bind(tracks[position])
    }

    @SuppressLint("NotifyDataSetChanged")
    fun updateData(newTracks: List<PlaylistApiResponse>) {
        tracks.clear()
        tracks.addAll(newTracks)
        notifyDataSetChanged()
    }
}