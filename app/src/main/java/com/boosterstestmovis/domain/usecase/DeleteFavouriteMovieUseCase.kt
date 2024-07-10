package com.boosterstestmovis.domain.usecase

import com.boosterstestmovis.domain.MovieRepository
import com.boosterstestmovis.domain.entity.FavouriteMovie
import javax.inject.Inject

class DeleteFavouriteMovieUseCase @Inject constructor(
    private val repository: MovieRepository
) {
    suspend operator fun invoke(movie: FavouriteMovie) = repository.deleteFavouriteMovie(movie)
}
