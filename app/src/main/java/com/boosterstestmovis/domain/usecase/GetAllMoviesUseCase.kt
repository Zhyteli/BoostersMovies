package com.boosterstestmovis.domain.usecase

import com.boosterstestmovis.domain.MovieRepository
import javax.inject.Inject

class GetAllMoviesUseCase @Inject constructor(
    private val repository: MovieRepository
) {
    operator fun invoke() = repository.getAllMovies()
}