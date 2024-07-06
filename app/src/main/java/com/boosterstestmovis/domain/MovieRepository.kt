package com.boosterstestmovis.domain

import com.boosterstestmovis.domain.entity.FavouriteMovie
import com.boosterstestmovis.domain.entity.Movie
import kotlinx.coroutines.flow.Flow

interface MovieRepository {
    fun getAllMovies(): Flow<List<Movie>>
    fun getAllFavouriteMovies(): Flow<List<FavouriteMovie>>
    suspend fun getMovieById(movieId: Int): Movie
    suspend fun getFavouriteMovieById(movieId: Int): FavouriteMovie
    suspend fun deleteAllMovies()
    suspend fun insertMovie(movies: List<Movie>)
    suspend fun deleteMovie(movie: Movie)
    suspend fun insertFavouriteMovie(movie: FavouriteMovie)
    suspend fun deleteFavouriteMovie(movie: FavouriteMovie)
}
