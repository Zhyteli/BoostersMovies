package com.boosterstestmovis

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.room.Room
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import com.boosterstestmovis.data.MovieDao
import com.boosterstestmovis.data.MovieDatabase
import com.boosterstestmovis.data.MovieRepositoryImpl
import com.boosterstestmovis.domain.MovieRepository
import com.boosterstestmovis.domain.entity.FavouriteMovie
import com.boosterstestmovis.domain.entity.Movie
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import org.junit.*
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
@ExperimentalCoroutinesApi
class MovieRepositoryImplTest {

    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    private lateinit var movieDatabase: MovieDatabase
    private lateinit var movieDao: MovieDao
    private lateinit var movieRepository: MovieRepository

    @Before
    fun setup() {
        val context = InstrumentationRegistry.getInstrumentation().targetContext
        movieDatabase = Room.inMemoryDatabaseBuilder(context, MovieDatabase::class.java)
            .allowMainThreadQueries()
            .build()
        movieDao = movieDatabase.movieDao()
        movieRepository = MovieRepositoryImpl(movieDao)
    }

    @After
    fun teardown() {
        movieDatabase.close()
    }

    @Test
    fun insertAndRetrieveMovies() = runBlocking {
        val movies = listOf(
            Movie(
                id = 1,
                voteCount = 100,
                title = "Movie Title 1",
                originalTitle = "Original Title 1",
                overview = "This is a short description of the first movie.",
                posterPath = "/k1VK2L971GmEevIjFbjcimxSeMx.jpg",
                backdropPath = "",
                voteAverage = 4.5,
                releaseDate = "2024-07-09"
            ),

            Movie(
                id = 2,
                voteCount = 200,
                title = "Movie Title 2",
                originalTitle = "Original Title 2",
                overview = "This is a short description of the first movie.",
                posterPath = "/k1VK2L971GmEevIjFbjcimxSeMx.jpg",
                backdropPath = "",
                voteAverage = 4.5,
                releaseDate = "2024-07-09"
            )
        )

        movieRepository.insertMovie(movies)
        val retrievedMovies = movieRepository.getAllMovies().first()

        Assert.assertEquals(movies, retrievedMovies)
    }

    @Test
    fun insertAndRetrieveFavouriteMovies() = runBlocking {
        val favouriteMovie = FavouriteMovie(
            uniqueId = 1,
            id = 1,
            voteCount = 100,
            title = "Movie Title 1",
            originalTitle = "Original Title 1",
            overview = "This is a short description of the first movie.",
            posterPath = "/k1VK2L971GmEevIjFbjcimxSeMx.jpg",
            backdropPath = "",
            voteAverage = 4.5,
            releaseDate = "2024-07-09")

        movieRepository.insertFavouriteMovie(favouriteMovie)
        val retrievedFavouriteMovies = movieRepository.getAllFavouriteMovies().first()

        Assert.assertTrue(retrievedFavouriteMovies.contains(favouriteMovie))
    }

    @Test
    fun deleteMovie() = runBlocking {
        val movie = Movie(
            id = 1,
            voteCount = 100,
            title = "Movie Title 1",
            originalTitle = "Original Title 1",
            overview = "This is a short description of the first movie.",
            posterPath = "/k1VK2L971GmEevIjFbjcimxSeMx.jpg",
            backdropPath = "",
            voteAverage = 4.5,
            releaseDate = "2024-07-09")

        movieRepository.insertMovie(listOf(movie))
        movieRepository.deleteMovie(movie)
        val retrievedMovies = movieRepository.getAllMovies().first()

        Assert.assertFalse(retrievedMovies.contains(movie))
    }

    @Test
    fun deleteFavouriteMovie() = runBlocking {
        val favouriteMovie = FavouriteMovie(uniqueId = 1,
            id = 1,
            voteCount = 100,
            title = "Movie Title 1",
            originalTitle = "Original Title 1",
            overview = "This is a short description of the first movie.",
            posterPath = "/k1VK2L971GmEevIjFbjcimxSeMx.jpg",
            backdropPath = "",
            voteAverage = 4.5,
            releaseDate = "2024-07-09")

        movieRepository.insertFavouriteMovie(favouriteMovie)
        movieRepository.deleteFavouriteMovie(favouriteMovie)
        val retrievedFavouriteMovies = movieRepository.getAllFavouriteMovies().first()

        Assert.assertFalse(retrievedFavouriteMovies.contains(favouriteMovie))
    }
}
