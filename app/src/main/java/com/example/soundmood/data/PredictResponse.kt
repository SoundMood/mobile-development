package com.example.soundmood.data

import com.google.gson.annotations.SerializedName

data class PredictResponse(

	@field:SerializedName("playlist_id")
	val playlistId: String? = null,

	@field:SerializedName("message")
	val message: String? = null
)
