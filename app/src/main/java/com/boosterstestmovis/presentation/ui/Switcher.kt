package com.boosterstestmovis.presentation.ui

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.lifecycle.viewmodel.compose.viewModel
import com.boosterstestmovis.presentation.ui.item.empty.EmptyListAnim
import com.boosterstestmovis.presentation.viewmodel.MovieViewModel

@Composable
fun Switcher(model: MovieViewModel = viewModel()) {
    val empty = model.emptyList.collectAsState().value
    val movies = model.movieList.collectAsState().value
    if (empty) {
        EmptyListAnim()
    } else {
        MainScreen(movies, model)
    }
}