package com.iwelogic.minecraft.mods.models

import com.google.gson.annotations.SerializedName

data class Skins(

	@field:SerializedName("skins")
	var skins: List<SkinsItem?>? = null,

	@field:SerializedName("serialize_name")
	var serializeName: String? = null,

	@field:SerializedName("localization_name")
	var localizationName: String? = null
)

data class SkinsItem(

	@field:SerializedName("localization_name")
	var localizationName: String? = null,

	@field:SerializedName("texture")
	var texture: String? = null,

	@field:SerializedName("geometry")
	var geometry: String? = null,

	@field:SerializedName("type")
	var type: String? = null
)
