package com.boosterstestmovis.data

import com.boosterstestmovis.domain.MovieRepository
import com.boosterstestmovis.domain.entity.FavouriteMovie
import com.boosterstestmovis.domain.entity.Movie
import kotlinx.coroutines.flow.Flow

class MovieRepositoryImpl(private val movieDao: MovieDao) : MovieRepository {

    override fun getAllMovies(): Flow<List<Movie>> {
        return movieDao.getAllMovies()
    }

    override fun getAllFavouriteMovies(): Flow<List<FavouriteMovie>> {
        return movieDao.getAllFavouriteMovies()
    }

    override suspend fun getMovieById(movieId: Int): Movie {
        return movieDao.getMovieById(movieId)
    }

    override suspend fun getFavouriteMovieById(movieId: Int): FavouriteMovie {
        return movieDao.getFavouriteMovieById(movieId)
    }

    override suspend fun deleteAllMovies() {
        movieDao.deleteAllMovies()
    }

    override suspend fun insertMovie(movies: List<Movie>) {
        movieDao.insertMovie(movies)
    }

    override suspend fun deleteMovie(movie: Movie) {
        movieDao.deleteMovie(movie)
    }

    override suspend fun insertFavouriteMovie(movie: FavouriteMovie) {
        movieDao.insertFavouriteMovie(movie)
    }

    override suspend fun deleteFavouriteMovie(movie: FavouriteMovie) {
        movieDao.deleteFavouriteMovie(movie)
    }
}
