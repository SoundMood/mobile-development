package com.example.soundmood.data

data class RecommendationResponse(
    val tracks: Tracks
)

data class Tracks(
    val href: String,
    val total: Int,
    val items: List<Track>
)

data class Track(
    val id: String,
    val name: String,
    val artists: List<Artist>,
    val album: Album,
    val external_urls: ExternalUrls
)

data class Artist(
    val name: String,
    val external_urls: ExternalUrls
)

data class Album(
    val name: String,
    val images: List<Image>
)

data class Image(
    val url: String,
    val height: Int,
    val width: Int
)

data class ExternalUrls(
    val spotify: String
)
