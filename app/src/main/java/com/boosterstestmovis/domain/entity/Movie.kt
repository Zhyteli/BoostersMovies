package com.boosterstestmovis.domain.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.Ignore
import com.google.gson.annotations.SerializedName
import com.google.gson.annotations.Expose

@Entity(tableName = "movies")
open class Movie(
    @PrimaryKey(autoGenerate = true)
    val uniqueId: Int = 0,
    @SerializedName("id")
    @Expose
    val id: Int,
    @SerializedName("vote_count")
    @Expose
    val voteCount: Int,
    @SerializedName("title")
    @Expose
    val title: String,
    @SerializedName("original_title")
    @Expose
    val originalTitle: String,
    @SerializedName("overview")
    @Expose
    val overview: String,
    @SerializedName("poster_path")
    @Expose
    val posterPath: String,
    val bigPosterPath: String,
    @SerializedName("backdrop_path")
    @Expose
    val backdropPath: String,
    @SerializedName("vote_average")
    @Expose
    val voteAverage: Double,
    @SerializedName("release_date")
    @Expose
    val releaseDate: String
) {
    @Ignore
    constructor(
        id: Int,
        voteCount: Int,
        title: String,
        originalTitle: String,
        overview: String,
        posterPath: String,
        bigPosterPath: String,
        backdropPath: String,
        voteAverage: Double,
        releaseDate: String
    ) : this(
        0, id, voteCount, title, originalTitle, overview, posterPath, bigPosterPath, backdropPath, voteAverage, releaseDate
    )

    override fun toString(): String {
        return "Movie(uniqueId=$uniqueId, id=$id, voteCount=$voteCount, title='$title', originalTitle='$originalTitle', overview='$overview', posterPath='$posterPath', bigPosterPath='$bigPosterPath', backdropPath='$backdropPath', voteAverage=$voteAverage, releaseDate='$releaseDate')"
    }

}
