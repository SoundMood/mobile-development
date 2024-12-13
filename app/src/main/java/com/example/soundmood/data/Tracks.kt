package com.example.soundmood.data

data class AddTracksRequest(
    val uris: List<String?> // Format URI Spotify, contoh: "spotify:track:{track_id}"
)