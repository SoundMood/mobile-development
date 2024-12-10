package com.example.soundmood.data

import com.google.gson.annotations.SerializedName

data class UserProfile(
	@SerializedName("country")
	val country: String? = null,

	@SerializedName("display_name")
	val displayName: String? = null,

	@SerializedName("email")
	val email: String? = null,

	@SerializedName("explicit_content")
	val explicitContent: ExplicitContent? = null,

	@SerializedName("external_urls")
	val externalUrls: ExternalUrls? = null,

	@SerializedName("followers")
	val followers: Followers? = null,

	@SerializedName("href")
	val href: String? = null,

	@SerializedName("id")
	val id: String? = null,

	@SerializedName("images")
	val images: List<ImagesItem>? = null,

	@SerializedName("product")
	val product: String? = null,

	@SerializedName("type")
	val type: String? = null,

	@SerializedName("uri")
	val uri: String? = null
)

data class ExplicitContent(
	@SerializedName("filter_enabled")
	val filterEnabled: Boolean? = null,

	@SerializedName("filter_locked")
	val filterLocked: Boolean? = null
)

data class ExternalUrls(
	@SerializedName("spotify")
	val spotify: String? = null
)

data class Followers(
	@SerializedName("href")
	val href: String? = null,

	@SerializedName("total")
	val total: Int? = null
)

data class ImagesItem(
	@SerializedName("height")
	val height: Int? = null,

	@SerializedName("url")
	val url: String? = null,

	@SerializedName("width")
	val width: Int? = null
)
