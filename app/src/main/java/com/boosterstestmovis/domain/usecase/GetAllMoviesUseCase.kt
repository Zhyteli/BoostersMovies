package com.boosterstestmovis.domain.usecase

import com.boosterstestmovis.domain.MovieRepository

class GetAllMoviesUseCase(private val repository: MovieRepository) {
    operator fun invoke() = repository.getAllMovies()
}