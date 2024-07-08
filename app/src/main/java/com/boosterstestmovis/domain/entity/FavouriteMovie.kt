package com.boosterstestmovis.domain.entity

import androidx.room.Entity
import androidx.room.Ignore

@Entity(tableName = "favourite_movies")
class FavouriteMovie : Movie {
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
    ) : super(
        uniqueId,
        id,
        voteCount,
        title,
        originalTitle,
        overview,
        posterPath,
        backdropPath,
        voteAverage,
        releaseDate
    )

    @Ignore
    constructor(movie: Movie) : super(
        movie.uniqueId,
        movie.id,
        movie.voteCount,
        movie.title,
        movie.originalTitle,
        movie.overview,
        movie.posterPath,
        movie.backdropPath,
        movie.voteAverage,
        movie.releaseDate
    )
}

