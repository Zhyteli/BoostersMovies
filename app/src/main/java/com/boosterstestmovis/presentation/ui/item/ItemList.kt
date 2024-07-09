package com.boosterstestmovis.presentation.ui.item

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.boosterstestmovis.domain.entity.Movie

@Composable
fun ItemList(list: List<Movie>, onLoadMore: () -> Unit) {
    LazyColumn(
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        itemsIndexed(list) { index, movie ->
            MovieCardItem(
                posterPath = movie.posterPath,
                movieName = movie.title,
                description = movie.overview,
                rating = movie.voteAverage.toDouble()
            )
            // Check if we have reached the end of the list
            if (index == list.size - 3) {
                onLoadMore()
            }
        }
        // Add a loading indicator at the end
        item {
            CircularProgressIndicator(modifier = Modifier.padding(16.dp).fillMaxWidth())
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
    ){

    }
}