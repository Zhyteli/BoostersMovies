package com.boosterstestmovis.presentation.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.boosterstestmovis.data.api.ApiParams
import com.boosterstestmovis.data.api.ApiService
import com.boosterstestmovis.data.api.connectivity.NetworkUtil
import com.boosterstestmovis.data.api.connectivity.SettingsResponse
import com.boosterstestmovis.domain.entity.FavouriteMovie
import com.boosterstestmovis.domain.entity.Movie
import com.boosterstestmovis.domain.usecase.DeleteFavouriteMovieUseCase
import com.boosterstestmovis.domain.usecase.GetAllFavouriteMoviesUseCase
import com.boosterstestmovis.domain.usecase.GetAllMoviesUseCase
import com.boosterstestmovis.domain.usecase.GetFavouriteMovieByIdUseCase
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
    private val getFavouriteMovieByIdUseCase: GetFavouriteMovieByIdUseCase,
    private val insertMovieUseCase: InsertMovieUseCase,
    private val insertFavouriteMovieUseCase: InsertFavouriteMovieUseCase,
    private val deleteFavouriteMovieUseCase: DeleteFavouriteMovieUseCase,
    private val apiService: ApiService
) : AndroidViewModel(application) {

    private val state = MutableStateFlow<StateScreenUI>(StateScreenUI.Loading)
    private val _movieList = MutableStateFlow<List<Movie>>(emptyList())
    val movieList: StateFlow<List<Movie>> get() = _movieList

    private val _emptyList = MutableStateFlow(true)
    val emptyList: StateFlow<Boolean> get() = _emptyList

    private val _errorVis = MutableStateFlow("")
    val errorVis: StateFlow<String> get() = _errorVis

    private var currentPage = 0

    init {
        startState()
    }

    private fun fetchMoviesFromApi(page: Int) {
        viewModelScope.launch {
            try {
                val response = apiService.getMovieResponse(
                    language = Locale.getDefault().language,
                    sort = SettingsResponse.PRIMARY_RELEASE_DATA,
                    minVoteCountValue = SettingsResponse.minVoteCountValue,
                    page = page.toString()
                )
                if (response.isSuccessful) {
                    val movies = response.body()?.movies.orEmpty()
                    state.emit(StateScreenUI.Content(movies))
                    _movieList.value += movies
                    if (page == 1) {
                        insertMovieUseCase(movies)
                    }
                } else {
                    handleError(response.errorBody()?.string().toString())
                }
            } catch (e: Exception) {
                handleError(e.message.toString())
            }
        }
    }

    private fun handleError(errorMessage: String) {
        state.value = StateScreenUI.Error(errorMessage)
        fetchMoviesFromDb()
    }

    private fun loadingMovies() {
        viewModelScope.launch {
            if (NetworkUtil.isNetworkAvailable(application)) {
                currentPage++
                fetchMoviesFromApi(currentPage)
            } else {
                fetchMoviesFromDbIfEmpty()
            }
        }
    }

    private suspend fun fetchMoviesFromDbIfEmpty() {
        movieList.collect {
            if (it.isEmpty()) {
                fetchMoviesFromDb()
            }
        }
    }

    fun loadingMore() {
        state.tryEmit(StateScreenUI.LoadingMore)
    }

    fun update() {
        state.tryEmit(StateScreenUI.Refreshing)
    }

    private fun startState() {
        viewModelScope.launch {
            state.collect { state ->
                when (state) {
                    StateScreenUI.Loading -> {
                        if (currentPage == 0) loadingMovies()
                    }
                    is StateScreenUI.Content -> _emptyList.value = false
                    is StateScreenUI.Error -> _errorVis.value = state.error
                    StateScreenUI.LoadingMore -> loadingMovies()
                    StateScreenUI.Refreshing -> refreshMovies()
                }
            }
        }
    }

    private fun refreshMovies() {
        viewModelScope.launch {
            currentPage = 0
            _emptyList.value = true
            delay(1000)
            loadingMovies()
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

    val allFavouriteMovies: StateFlow<List<FavouriteMovie>> = getAllFavouriteMoviesUseCase()
        .stateIn(viewModelScope, SharingStarted.Lazily, emptyList())

    fun getFavouriteMovieById(movieId: Int): Flow<FavouriteMovie> {
        return flow { emit(getFavouriteMovieByIdUseCase(movieId)) }
    }

    fun insertFavouriteMovie(movie: FavouriteMovie) = viewModelScope.launch {
        insertFavouriteMovieUseCase(movie)
    }

    fun deleteFavouriteMovie(movie: FavouriteMovie) = viewModelScope.launch {
        deleteFavouriteMovieUseCase(movie)
    }
}