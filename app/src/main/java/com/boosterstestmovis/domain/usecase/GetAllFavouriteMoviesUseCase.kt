package com.boosterstestmovis.domain.usecase

import com.boosterstestmovis.domain.MovieRepository
import javax.inject.Inject

class GetAllFavouriteMoviesUseCase @Inject constructor(
    private val repository: MovieRepository
) {
    operator fun invoke() = repository.getAllFavouriteMovies()
}
