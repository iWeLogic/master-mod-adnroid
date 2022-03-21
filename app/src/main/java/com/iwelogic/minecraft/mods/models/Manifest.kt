package com.iwelogic.minecraft.mods.models

import com.google.gson.annotations.SerializedName

data class Manifest(

	@field:SerializedName("format_version")
	var formatVersion: Int? = null,

	@field:SerializedName("header")
	var header: Header? = null,

	@field:SerializedName("modules")
	var modules: List<ModulesItem>? = null
)

data class Header(

	@field:SerializedName("name")
	var name: String? = null,

	@field:SerializedName("uuid")
	var uuid: String? = null,

	@field:SerializedName("version")
	var version: List<Int?>? = null
)

data class ModulesItem(

	@field:SerializedName("type")
	var type: String? = null,

	@field:SerializedName("uuid")
	var uuid: String? = null,

	@field:SerializedName("version")
	var version: List<Int?>? = null
)
