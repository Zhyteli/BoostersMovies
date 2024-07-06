package com.boosterstestmovis.domain.usecase

import com.boosterstestmovis.domain.MovieRepository
import com.boosterstestmovis.domain.entity.Movie

class DeleteMovieUseCase(private val repository: MovieRepository) {
    suspend operator fun invoke(movie: Movie) = repository.deleteMovie(movie)
}
