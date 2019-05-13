package com.example.myapplication
import com.google.gson.annotations.SerializedName



data class Json4Kotlin_Base (

	@SerializedName("type") val type : String,
	@SerializedName("value") val value : List<Value>
)