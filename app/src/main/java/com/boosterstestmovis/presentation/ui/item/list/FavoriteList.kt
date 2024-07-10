package com.boosterstestmovis.presentation.ui.item.list

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.boosterstestmovis.domain.entity.FavouriteMovie
import com.boosterstestmovis.presentation.ui.item.MovieCardItem
import java.time.LocalDate
import java.time.YearMonth
import java.time.format.DateTimeFormatter

@Composable
fun FavoriteList(list: List<FavouriteMovie>) {
    val uniqueList = list.distinctBy { it.id }
    val dateFormatter = DateTimeFormatter.ofPattern("MMM yyyy")
    val groupedMovies = uniqueList.groupBy { YearMonth.from(LocalDate.parse(it.releaseDate)) }
        .toSortedMap(reverseOrder())

    val scrollState = rememberLazyListState()
    var showButton by remember { mutableStateOf(false) }
    var previousIndex by remember { mutableStateOf(0) }
    var previousScrollOffset by remember { mutableStateOf(0) }

    LaunchedEffect(scrollState) {
        snapshotFlow { scrollState.firstVisibleItemIndex to scrollState.firstVisibleItemScrollOffset }
            .collect { (currentIndex, currentScrollOffset) ->
                showButton = when {
                    currentIndex > previousIndex -> false
                    currentIndex < previousIndex -> true
                    currentScrollOffset < previousScrollOffset -> true
                    else -> false
                }
                previousIndex = currentIndex
                previousScrollOffset = currentScrollOffset
            }
    }

    Box(modifier = Modifier.fillMaxSize()) {
        LazyColumn(
            state = scrollState,
            contentPadding = PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            groupedMovies.forEach { (yearMonth, movies) ->
                item {
                    Text(
                        text = yearMonth.format(dateFormatter),
                        style = MaterialTheme.typography.headlineSmall.copy(
                            fontSize = 20.sp,
                            fontWeight = FontWeight.Bold
                        ),
                        modifier = Modifier.padding(vertical = 8.dp)
                    )
                }
                itemsIndexed(movies) { index, movie ->
                    MovieCardItem(movie)
                }
            }
            // Add a loading indicator at the end
            item {
                CircularProgressIndicator(
                    modifier = Modifier
                        .padding(16.dp)
                        .fillMaxWidth()
                )
            }
        }
    }
}
