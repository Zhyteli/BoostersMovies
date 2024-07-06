package com.boosterstestmovis.domain.entity

import androidx.room.Entity
import androidx.room.Ignore

@Entity(tableName = "favourite_movies")
class FavouriteMovie(
    uniqueId: Int,
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
) : Movie(
    uniqueId,
    id,
    voteCount,
    title,
    originalTitle,
    overview,
    posterPath,
    bigPosterPath,
    backdropPath,
    voteAverage,
    releaseDate
) {
    @Ignore
    constructor(movie: Movie) : this(
        movie.uniqueId,
        movie.id,
        movie.voteCount,
        movie.title,
        movie.originalTitle,
        movie.overview,
        movie.posterPath,
        movie.bigPosterPath,
        movie.backdropPath,
        movie.voteAverage,
        movie.releaseDate
    )

}
