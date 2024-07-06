package com.boosterstestmovis.domain.usecase

import com.boosterstestmovis.domain.MovieRepository

class GetAllFavouriteMoviesUseCase(private val repository: MovieRepository) {
    operator fun invoke() = repository.getAllFavouriteMovies()
}
