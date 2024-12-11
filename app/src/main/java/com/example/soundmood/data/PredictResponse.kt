package com.example.soundmood.data

import com.google.gson.annotations.SerializedName

data class PredictResponse(

	@field:SerializedName("mood")
	val mood: String? = null,

	@field:SerializedName("user_id")
	val userId: String? = null,

	@field:SerializedName("song_ids")
	val songIds: List<String?>? = null,

	@field:SerializedName("name")
	val name: String? = null,

	@field:SerializedName("created_at")
	val createdAt: String? = null,

	@field:SerializedName("id")
	val id: String? = null,

	@field:SerializedName("photo_url")
	val photoUrl: Any? = null
)
