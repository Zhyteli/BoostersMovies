package com.boosterstestmovis.data.pojo

import com.boosterstestmovis.domain.entity.Trailer
import com.google.gson.annotations.SerializedName
import com.google.gson.annotations.Expose

class TrailerResponse {
    @SerializedName("id")
    @Expose
    var id: Int = 0

    @SerializedName("results")
    @Expose
    var trailers: ArrayList<Trailer>? = null
}
