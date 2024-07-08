package com.boosterstestmovis.data.pojo

import com.boosterstestmovis.domain.entity.Review
import com.google.gson.annotations.SerializedName
import com.google.gson.annotations.Expose

data class ReviewResponse(
    @SerializedName("id")
    @Expose
    var id: Int = 0,

    @SerializedName("page")
    @Expose
    var page: Int = 0,

    @SerializedName("results")
    @Expose
    var reviews: List<Review>? = null,

    @SerializedName("total_pages")
    @Expose
    var totalPages: Int = 0,

    @SerializedName("total_results")
    @Expose
    var totalResults: Int = 0
)