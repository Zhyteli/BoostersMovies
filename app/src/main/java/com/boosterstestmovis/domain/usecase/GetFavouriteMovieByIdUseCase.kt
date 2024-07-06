package com.boosterstestmovis.domain.usecase

import com.boosterstestmovis.domain.MovieRepository

class GetFavouriteMovieByIdUseCase(private val repository: MovieRepository) {
    suspend operator fun invoke(movieId: Int) = repository.getFavouriteMovieById(movieId)
}
