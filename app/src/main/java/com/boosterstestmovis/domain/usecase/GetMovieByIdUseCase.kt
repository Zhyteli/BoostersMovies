package com.boosterstestmovis.domain.usecase

import com.boosterstestmovis.domain.MovieRepository

class GetMovieByIdUseCase(private val repository: MovieRepository) {
    suspend operator fun invoke(movieId: Int) = repository.getMovieById(movieId)
}
