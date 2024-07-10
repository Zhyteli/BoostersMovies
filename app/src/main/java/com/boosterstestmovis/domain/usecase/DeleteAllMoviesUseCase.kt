package com.boosterstestmovis.domain.usecase

import com.boosterstestmovis.domain.MovieRepository
import javax.inject.Inject

class DeleteAllMoviesUseCase @Inject constructor(
    private val repository: MovieRepository
) {
    suspend operator fun invoke() = repository.deleteAllMovies()
}
