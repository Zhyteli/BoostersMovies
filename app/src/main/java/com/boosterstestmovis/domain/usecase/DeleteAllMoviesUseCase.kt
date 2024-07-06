package com.boosterstestmovis.domain.usecase

import com.boosterstestmovis.domain.MovieRepository

class DeleteAllMoviesUseCase(private val repository: MovieRepository) {
    suspend operator fun invoke() = repository.deleteAllMovies()
}
