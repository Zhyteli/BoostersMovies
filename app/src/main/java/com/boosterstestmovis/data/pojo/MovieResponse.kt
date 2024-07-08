package com.boosterstestmovis.data.pojo

import com.boosterstestmovis.domain.entity.Movie
import com.google.gson.annotations.SerializedName
import com.google.gson.annotations.Expose
import com.squareup.moshi.Json

data class MovieResponse(
    @SerializedName("page")
    @Expose
    var page: Int = 0,

    @SerializedName("results")
    @Expose
    var movies: List<Movie>? = null,

    @SerializedName("total_pages")
    @Expose
    var totalPages: Int = 0,

    @SerializedName("total_results")
    @Expose
    var totalMovies: Int = 0
)
