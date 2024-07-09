package com.boosterstestmovis.presentation.viewmodel

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.boosterstestmovis.data.api.ApiParams
import com.boosterstestmovis.data.api.ApiService
import com.boosterstestmovis.data.api.connectivity.NetworkUtil
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
import com.boosterstestmovis.presentation.ui.state.StateScreenUI
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import java.util.Locale
import javax.inject.Inject

@HiltViewModel
class MovieViewModel @Inject constructor(
    private val application: Application,

    private val getAllMoviesUseCase: GetAllMoviesUseCase,
    private val getAllFavouriteMoviesUseCase: GetAllFavouriteMoviesUseCase,
    private val getMovieByIdUseCase: GetMovieByIdUseCase,
    private val getFavouriteMovieByIdUseCase: GetFavouriteMovieByIdUseCase,
    private val deleteAllMoviesUseCase: DeleteAllMoviesUseCase,
    private val insertMovieUseCase: InsertMovieUseCase,
    private val deleteMovieUseCase: DeleteMovieUseCase,
    private val insertFavouriteMovieUseCase: InsertFavouriteMovieUseCase,
    private val deleteFavouriteMovieUseCase: DeleteFavouriteMovieUseCase,

    private val apiService: ApiService
) : AndroidViewModel(application) {

    private val state = MutableStateFlow<StateScreenUI>(StateScreenUI.Loading)
    private val _movieList = MutableStateFlow<List<Movie>>(emptyList())
    val movieList: StateFlow<List<Movie>> get() = _movieList

    private val _emptyList = MutableStateFlow(true)
    val emptyList: StateFlow<Boolean> get() = _emptyList

    private fun loadingMovies() {
        viewModelScope.launch {
            if (NetworkUtil.isNetworkAvailable(application)) {
                fetchMoviesFromApi()
            } else {
                movieList.collect {
                    if (it.isEmpty()) {
                        fetchMoviesFromDb()
                    }
                }
            }
        }
    }

    fun startState() {
        viewModelScope.launch {
            state.collect {
                when (it) {
                    StateScreenUI.Loading -> {
                        loadingMovies()
                    }
                    is StateScreenUI.Content -> {
                        _emptyList.value = false
                        _movieList.value = it.movie
                    }

                    is StateScreenUI.Error -> {

                    }


                    StateScreenUI.LoadingMore -> {

                    }

                    StateScreenUI.Refreshing -> {

                    }
                }
            }
        }
    }

    private fun fetchMoviesFromApi() {
        viewModelScope.launch {
            try {
                val response = apiService.getMovieResponse(
                    language = Locale.getDefault().language,
                    sort = ApiParams.PRIMARY_RELEASE_DATA,
                    minVoteCountValue = "100",
                    page = "1"
                )
                if (response.isSuccessful) {
                    val movie = response.body()?.movies
                    val page = response.body()?.page
                    Log.d("TEST_1_GET_MOVIE", movie.toString())
                    if (movie != null) {
                        state.emit(StateScreenUI.Content(movie))
                        if (page == 1) {
                            insertMovieUseCase(movie)
                        }
                    }
                } else {
                    val error = response.errorBody()?.string().toString()
                    Log.d("TEST_2_GET_ERROR_RESPONSE", error)
                    fetchMoviesFromDb() // Fallback to DB in case of API error
                }
            } catch (e: Exception) {
                Log.d("TEST_FATAL", e.toString())
                fetchMoviesFromDb() // Fallback to DB in case of exception
            }
        }
    }

    private fun fetchMoviesFromDb() {
        viewModelScope.launch {
            val movies = getAllMoviesUseCase().firstOrNull().orEmpty()
            if (movies.isNotEmpty()) {
                state.emit(StateScreenUI.Content(movies))
            }
        }
    }


    private val allMovies: StateFlow<List<Movie>> = getAllMoviesUseCase()
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