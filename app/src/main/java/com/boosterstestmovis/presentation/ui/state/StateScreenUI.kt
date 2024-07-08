package com.boosterstestmovis.presentation.ui.state

import com.boosterstestmovis.domain.entity.Movie

sealed class StateScreenUI {
    data object Loading : StateScreenUI()
    data object Refreshing : StateScreenUI()
    data object LoadingMore : StateScreenUI()
    data class Error(val error: String) : StateScreenUI()
    data class Content(val movie: List<Movie>) : StateScreenUI()
}