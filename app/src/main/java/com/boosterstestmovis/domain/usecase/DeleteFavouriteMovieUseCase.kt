package com.boosterstestmovis.domain.usecase

import com.boosterstestmovis.domain.MovieRepository
import com.boosterstestmovis.domain.entity.FavouriteMovie

class DeleteFavouriteMovieUseCase(private val repository: MovieRepository) {
    suspend operator fun invoke(movie: FavouriteMovie) = repository.deleteFavouriteMovie(movie)
}
