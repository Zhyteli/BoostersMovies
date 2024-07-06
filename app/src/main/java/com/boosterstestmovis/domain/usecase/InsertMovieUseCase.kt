package com.boosterstestmovis.domain.usecase

import com.boosterstestmovis.domain.MovieRepository
import com.boosterstestmovis.domain.entity.Movie

class InsertMovieUseCase(private val repository: MovieRepository) {
    suspend operator fun invoke(movies: List<Movie>) = repository.insertMovie(movies)
}
