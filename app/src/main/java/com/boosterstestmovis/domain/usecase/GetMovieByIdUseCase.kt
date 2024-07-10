package com.boosterstestmovis.domain.usecase

import com.boosterstestmovis.domain.MovieRepository
import javax.inject.Inject

class GetMovieByIdUseCase @Inject constructor(
    private val repository: MovieRepository
) {
    suspend operator fun invoke(movieId: Int) = repository.getMovieById(movieId)
}
