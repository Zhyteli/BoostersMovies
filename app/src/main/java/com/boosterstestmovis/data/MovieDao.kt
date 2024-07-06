package com.boosterstestmovis.data

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.boosterstestmovis.domain.entity.FavouriteMovie
import com.boosterstestmovis.domain.entity.Movie
import kotlinx.coroutines.flow.Flow

@Dao
interface MovieDao {
    @Query("SELECT * FROM movies")
    fun getAllMovies(): Flow<List<Movie>>

    @Query("SELECT * FROM favourite_movies")
    fun getAllFavouriteMovies(): Flow<List<FavouriteMovie>>

    @Query("SELECT * FROM movies WHERE id == :movieId")
    suspend fun getMovieById(movieId: Int): Movie

    @Query("SELECT * FROM favourite_movies WHERE id == :movieId")
    suspend fun getFavouriteMovieById(movieId: Int): FavouriteMovie

    @Query("DELETE FROM movies")
    suspend fun deleteAllMovies()

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertMovie(movies: List<Movie>)

    @Delete
    suspend fun deleteMovie(movie: Movie)

    @Insert
    suspend fun insertFavouriteMovie(movie: FavouriteMovie)

    @Delete
    suspend fun deleteFavouriteMovie(movie: FavouriteMovie)
}

