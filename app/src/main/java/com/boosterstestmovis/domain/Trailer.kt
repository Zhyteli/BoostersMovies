package com.boosterstestmovis.domain

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class Trailer(
    @SerializedName("key")
    @Expose
    val key:String,
    @SerializedName("name")
    @Expose
    val name:String
) {
}