package com.boosterstestmovis.data

import androidx.lifecycle.LiveData
import androidx.room.*
import com.boosterstestmovis.domain.FavouriteMovie
import com.boosterstestmovis.domain.Movie

@Dao
interface MovieDao {
    @Query("SELECT * FROM movies")
    fun getAllMovies(): LiveData<List<Movie>>

    @Query("SELECT * FROM favourite_movies")
    fun getAllFavouriteMovies(): LiveData<List<FavouriteMovie>>

    @Query("SELECT * FROM movies WHERE id == :movieId")
    fun getMovieById(movieId: Int): Movie

    @Query("SELECT * FROM favourite_movies WHERE id == :movieId")
    fun getFavouriteMovieById(movieId: Int): FavouriteMovie

    @Query("DELETE FROM movies")
    fun deleteAllMovies()

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertMovie(movies: List<Movie>)

    @Delete
    fun deleteMovie(movie: Movie)

    @Insert
    fun insertFavouriteMovie(movie: FavouriteMovie)

    @Delete
    fun deleteFavouriteMovie(movie: FavouriteMovie)
}
