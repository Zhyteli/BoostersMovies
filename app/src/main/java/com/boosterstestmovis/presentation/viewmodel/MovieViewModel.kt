package com.boosterstestmovis.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.boosterstestmovis.domain.entity.FavouriteMovie
import com.boosterstestmovis.domain.entity.Movie
import com.boosterstestmovis.domain.usecase.DeleteAllMoviesUseCase
import com.boosterstestmovis.domain.usecase.DeleteFavouriteMovieUseCase
import com.boosterstestmovis.domain.usecase.DeleteMovieUseCase
import com.boosterstestmovis.domain.usecase.GetAllFavouriteMoviesUseCase
import com.boosterstestmovis.domain.usecase.GetAllMoviesUseCase
import com.boosterstestmovis.domain.usecase.GetFavouriteMovieByIdUseCase
import com.boosterstestmovis.domain.usecase.GetMovieByIdUseCase
import com.boosterstestmovis.domain.usecase.InsertFavouriteMovieUseCase
import com.boosterstestmovis.domain.usecase.InsertMovieUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MovieViewModel @Inject constructor(
    private val getAllMoviesUseCase: GetAllMoviesUseCase,
    private val getAllFavouriteMoviesUseCase: GetAllFavouriteMoviesUseCase,
    private val getMovieByIdUseCase: GetMovieByIdUseCase,
    private val getFavouriteMovieByIdUseCase: GetFavouriteMovieByIdUseCase,
    private val deleteAllMoviesUseCase: DeleteAllMoviesUseCase,
    private val insertMovieUseCase: InsertMovieUseCase,
    private val deleteMovieUseCase: DeleteMovieUseCase,
    private val insertFavouriteMovieUseCase: InsertFavouriteMovieUseCase,
    private val deleteFavouriteMovieUseCase: DeleteFavouriteMovieUseCase
) : ViewModel() {

    val allMovies: StateFlow<List<Movie>> = getAllMoviesUseCase()
        .stateIn(viewModelScope, SharingStarted.Lazily, emptyList())

    val allFavouriteMovies: StateFlow<List<FavouriteMovie>> = getAllFavouriteMoviesUseCase()
        .stateIn(viewModelScope, SharingStarted.Lazily, emptyList())

    fun getMovieById(movieId: Int): Flow<Movie> {
        return flow { emit(getMovieByIdUseCase(movieId)) }
    }

    fun getFavouriteMovieById(movieId: Int): Flow<FavouriteMovie> {
        return flow { emit(getFavouriteMovieByIdUseCase(movieId)) }
    }

    fun insertMovie(movies: List<Movie>) = viewModelScope.launch {
        insertMovieUseCase(movies)
    }

    fun deleteMovie(movie: Movie) = viewModelScope.launch {
        deleteMovieUseCase(movie)
    }

    fun insertFavouriteMovie(movie: FavouriteMovie) = viewModelScope.launch {
        insertFavouriteMovieUseCase(movie)
    }

    fun deleteFavouriteMovie(movie: FavouriteMovie) = viewModelScope.launch {
        deleteFavouriteMovieUseCase(movie)
    }
}