package com.example.soundmood.data

import com.google.gson.annotations.SerializedName

data class CreatePlaylistResponse(

	@field:SerializedName("error")
	val error: Error? = null
)

data class Error(

	@field:SerializedName("message")
	val message: String? = null,

	@field:SerializedName("status")
	val status: Int? = null
)
