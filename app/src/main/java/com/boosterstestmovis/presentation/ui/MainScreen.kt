package com.boosterstestmovis.presentation.ui

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import com.boosterstestmovis.domain.entity.Movie
import com.boosterstestmovis.presentation.ui.item.list.FavoriteList
import com.boosterstestmovis.presentation.ui.item.list.ItemList
import com.boosterstestmovis.presentation.ui.switcher.CustomSwitch
import com.boosterstestmovis.presentation.viewmodel.MovieViewModel

@Composable
fun MainScreen(
    movies: List<Movie>,
    model: MovieViewModel
) {
    var isSwitchChecked by remember { mutableStateOf(true) }
    val favoriteMovies = model.allFavouriteMovies.collectAsState().value

    Box(Modifier.fillMaxSize()) {
        Column {
            CustomSwitch(
                isChecked = isSwitchChecked,
                onCheckedChange = { isSwitchChecked = it }
            )

            if (isSwitchChecked) {
                ItemList(movies,
                    onLoadMore = {
                        model.loadingMore()
                    },
                    onRefresh = {
                        model.update()
                    }
                )
            } else {
                FavoriteList(favoriteMovies)

            }
        }
    }
}
