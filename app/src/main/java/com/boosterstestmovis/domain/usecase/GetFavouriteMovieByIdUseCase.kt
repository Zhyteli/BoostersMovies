package com.boosterstestmovis.domain.usecase

import com.boosterstestmovis.domain.MovieRepository
import javax.inject.Inject

class GetFavouriteMovieByIdUseCase @Inject constructor(
    private val repository: MovieRepository
) {
    suspend operator fun invoke(movieId: Int) = repository.getFavouriteMovieById(movieId)
}
