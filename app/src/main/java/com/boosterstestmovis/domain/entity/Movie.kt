package com.boosterstestmovis.domain.entity

import androidx.room.Entity
import androidx.room.Ignore
import androidx.room.PrimaryKey
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

@Entity(tableName = "movies")
open class Movie {
    @PrimaryKey(autoGenerate = true)
    var uniqueId: Int = 0

    @SerializedName("adult")
    @Expose
    var isAdult: Boolean = false

    @SerializedName("id")
    @Expose
    var id: Int

    @SerializedName("vote_count")
    @Expose
    var voteCount: Int

    @SerializedName("title")
    @Expose
    var title: String

    @SerializedName("original_title")
    @Expose
    var originalTitle: String

    @SerializedName("overview")
    @Expose
    var overview: String

    @SerializedName("popularity")
    @Expose
    var popularity: Double = 0.0

    @SerializedName("poster_path")
    @Expose
    var posterPath: String

    @SerializedName("backdrop_path")
    @Expose
    var backdropPath: String

    @SerializedName("vote_average")
    @Expose
    var voteAverage: Double

    @SerializedName("release_date")
    @Expose
    var releaseDate: String

    constructor(
        uniqueId: Int,
        id: Int,
        voteCount: Int,
        title: String,
        originalTitle: String,
        overview: String,
        posterPath: String,
        backdropPath: String,
        voteAverage: Double,
        releaseDate: String
    ) {
        this.uniqueId = uniqueId
        this.id = id
        this.voteCount = voteCount
        this.title = title
        this.originalTitle = originalTitle
        this.overview = overview
        this.posterPath = posterPath
        this.backdropPath = backdropPath
        this.voteAverage = voteAverage
        this.releaseDate = releaseDate
    }

    @Ignore
    constructor(
        id: Int,
        voteCount: Int,
        title: String,
        originalTitle: String,
        overview: String,
        posterPath: String,
        backdropPath: String,
        voteAverage: Double,
        releaseDate: String
    ) {
        this.id = id
        this.voteCount = voteCount
        this.title = title
        this.originalTitle = originalTitle
        this.overview = overview
        this.posterPath = posterPath
        this.backdropPath = backdropPath
        this.voteAverage = voteAverage
        this.releaseDate = releaseDate
    }

    override fun toString(): String {
        return "Movie(uniqueId=$uniqueId, isAdult=$isAdult, id=$id, voteCount=$voteCount, title='$title', originalTitle='$originalTitle', overview='$overview', popularity=$popularity, posterPath='$posterPath', backdropPath='$backdropPath', voteAverage=$voteAverage, releaseDate='$releaseDate')"
    }

}

