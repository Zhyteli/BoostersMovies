package com.boosterstestmovis.domain.usecase

import com.boosterstestmovis.domain.MovieRepository
import com.boosterstestmovis.domain.entity.FavouriteMovie
import javax.inject.Inject

class InsertFavouriteMovieUseCase @Inject constructor(
    private val repository: MovieRepository
) {
    suspend operator fun invoke(movie: FavouriteMovie) = repository.insertFavouriteMovie(movie)
}
