package com.example.soundmood.data

import com.google.gson.annotations.SerializedName

data class TokenResponse(

	@field:SerializedName("app_token")
	val appToken: String? = null,

	@field:SerializedName("expire_at")
	val expireAt: String? = null
)
