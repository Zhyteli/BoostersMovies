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
import kotlinx.coroutines.delay
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

    private var currentPage = 0

    private fun fetchMoviesFromApi(page: Int) {
        viewModelScope.launch {
            try {
                val response = apiService.getMovieResponse(
                    language = Locale.getDefault().language,
                    sort = ApiParams.PRIMARY_RELEASE_DATA,
                    minVoteCountValue = "100",
                    page = page.toString()
                )
                if (response.isSuccessful) {
                    val movie = response.body()?.movies ?: emptyList()
                    state.emit(StateScreenUI.Content(movie))
                    _movieList.value += movie
                    if (page == 1) {
                        insertMovieUseCase(movie)
                    }
                } else {
                    fetchMoviesFromDb() // Fallback to DB in case of API error
                }
            } catch (e: Exception) {
                fetchMoviesFromDb() // Fallback to DB in case of exception
            }
        }
    }

    fun loadingMovies() {
        viewModelScope.launch {
            if (NetworkUtil.isNetworkAvailable(application)) {
                currentPage++
                fetchMoviesFromApi(currentPage)
            } else {
                movieList.collect {
                    if (it.isEmpty()) {
                        fetchMoviesFromDb()
                    }
                }
            }
        }
    }

    fun loadingMore(){
        state.tryEmit(StateScreenUI.LoadingMore)
    }

    fun update(){
        state.tryEmit(StateScreenUI.Refreshing)
    }

    init {
        startState()
    }

    fun startState() {
        viewModelScope.launch {
            state.collect {
                when (it) {
                    StateScreenUI.Loading -> {
                        if (currentPage == 0){
                            loadingMovies()
                        }
                    }

                    is StateScreenUI.Content -> {
                        _emptyList.value = false
                    }

                    is StateScreenUI.Error -> {

                    }


                    StateScreenUI.LoadingMore -> {
                        loadingMovies()
                    }

                    StateScreenUI.Refreshing -> {
                        currentPage = 0
                        _emptyList.value = true
                        viewModelScope.launch {
                            delay(1000)
                            loadingMovies()
                        }
                    }
                }
            }
        }
    }

    private fun fetchMoviesFromDb() {
        viewModelScope.launch {
            val movies = getAllMoviesUseCase().firstOrNull().orEmpty()
            if (movies.isNotEmpty()) {
                state.emit(StateScreenUI.Content(movies))
                _movieList.value += movies
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