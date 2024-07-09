package com.boosterstestmovis.presentation.ui.item

import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import com.boosterstestmovis.R
import com.boosterstestmovis.domain.entity.Movie

@Composable
fun ItemList(list: List<Movie>) {
    LazyColumn {
        items(list) { movie ->
            MovieCardItem(
                posterPath = movie.posterPath, // Replace with your image resource or use a loader for image URLs
                movieName = movie.title,
                description = movie.overview,
                rating = movie.voteAverage
            )
        }
    }
}
@Preview
@Composable
fun ItemListPreview() {
    ItemList(
        listOf(
            Movie(
                uniqueId = 1,
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
                uniqueId = 2,
                id = 2,
                voteCount = 200,
                title = "Movie Title 2",
                originalTitle = "Original Title 2",
                overview = "This is a short description of the second movie.",
                posterPath = "",
                backdropPath = "",
                voteAverage = 3.8,
                releaseDate = "2024-07-09"
            )
        )
    )
}