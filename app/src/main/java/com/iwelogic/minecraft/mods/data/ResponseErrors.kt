package com.iwelogic.minecraft.mods.data

import com.google.gson.annotations.SerializedName

data class ResponseErrors(

	@field:SerializedName("message")
	val message: String? = null,

	@field:SerializedName("errors")
	val errors: Errors? = null
)

data class Errors(

	@field:SerializedName("fields")
	val fields: List<FieldsItem?>? = null
)

data class FieldsItem(

	@field:SerializedName("messages")
	val messages: List<String?>? = null,

	@field:SerializedName("type")
	val type: String? = null
)
