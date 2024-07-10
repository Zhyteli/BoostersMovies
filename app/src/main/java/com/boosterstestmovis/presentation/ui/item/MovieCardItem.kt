package com.boosterstestmovis.presentation.ui.item

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.VectorConverter
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.animateValue
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.AsyncImagePainter
import coil.compose.rememberAsyncImagePainter
import com.boosterstestmovis.domain.entity.FavouriteMovie
import com.boosterstestmovis.domain.entity.Movie
import com.boosterstestmovis.presentation.ui.item.ImageLink.BASE_POSTER_URL
import com.boosterstestmovis.presentation.ui.item.ImageLink.SMALL_POSTER_SIZE
import com.boosterstestmovis.presentation.ui.item.anim.LoadingImageAnim
import com.boosterstestmovis.presentation.viewmodel.MovieViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlin.random.Random

@Composable
fun MovieCardItem(
    movie: Movie,
    model: MovieViewModel = viewModel(),
) {
    val imageUrl = BASE_POSTER_URL + SMALL_POSTER_SIZE + movie.posterPath
    var isFavorite by remember { mutableStateOf(false) }
    var loading by remember { mutableStateOf(true) }
    val painter = rememberAsyncImagePainter(imageUrl)
    val favoriteState = model.allFavouriteMovies.collectAsState().value

    LaunchedEffect(painter) {
        snapshotFlow { painter.state }
            .collect { state ->
                loading = state is AsyncImagePainter.State.Loading
            }
    }

    // Check if the movie is a favorite and update the state accordingly
    LaunchedEffect(movie.id, favoriteState) {
        model.getFavouriteMovieById(movie.id).collect { favouriteMovie ->
            isFavorite = favouriteMovie != null
        }
    }

    MovieCard(
        loading,
        painter,
        movie.title,
        movie.overview,
        movie.voteAverage,
        movie.releaseDate,
        isFavorite
    ) {
        if (isFavorite) {
            // If the movie is already a favorite, remove it from favorites
            favoriteState.forEach {
                if (movie.id == it.id){
                    model.deleteFavouriteMovie(
                        it
                    )
                }
            }
        } else {
            // If the movie is not a favorite, add it to favorites
            model.insertFavouriteMovie(
                FavouriteMovie(
                    movie.uniqueId,
                    movie.id,
                    movie.voteCount,
                    movie.title,
                    movie.originalTitle,
                    movie.overview,
                    movie.posterPath,
                    movie.backdropPath,
                    movie.voteAverage,
                    movie.releaseDate
                )
            )
        }
        isFavorite = !isFavorite // Toggle favorite state
    }
}


@Composable
private fun MovieCard(
    loading: Boolean,
    painter: AsyncImagePainter,
    movieName: String,
    description: String,
    rating: Double,
    releaseDate: String,
    isFavorite: Boolean,
    onFavoriteClick: () -> Unit
) {
    var showSmallFavorites by remember { mutableStateOf(false) }

    Card(
        modifier = Modifier
            .padding(8.dp)
            .fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(
            containerColor = Color(0xFFFFCCCC)
        )
    ) {
        Row(
            modifier = Modifier
                .padding(16.dp)
        ) {
            Box {
                if (loading) {
                    LoadingImageAnim {
                        Image(
                            painter = painter,
                            contentDescription = null,
                            contentScale = ContentScale.Fit,
                            modifier = Modifier
                                .height(200.dp)
                                .width(135.dp)  // Adjust width as needed
                                .clip(RoundedCornerShape(8.dp))
                        )
                    }
                } else {
                    Image(
                        painter = painter,
                        contentDescription = null,
                        contentScale = ContentScale.Fit,
                        modifier = Modifier
                            .height(200.dp)
                            .width(135.dp)  // Adjust width as needed
                            .clip(RoundedCornerShape(8.dp))
                    )
                }
            }

            Spacer(modifier = Modifier.width(16.dp))
            Column(
                modifier = Modifier
                    .weight(1f)
            ) {
                Text(
                    text = movieName,
                    style = MaterialTheme.typography.headlineSmall.copy(
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold
                    )
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = description,
                    style = MaterialTheme.typography.bodyMedium.copy(
                        fontSize = 16.sp
                    ),
                    maxLines = 3,
                    overflow = TextOverflow.Ellipsis,
                    color = Color.DarkGray,
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = "Release Date: $releaseDate",
                    style = MaterialTheme.typography.bodyMedium.copy(
                        fontSize = 16.sp
                    ),
                    color = Color.DarkGray,
                )
                Spacer(modifier = Modifier.height(5.dp))
                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "Rating: $rating",
                        style = MaterialTheme.typography.bodyMedium.copy(
                            fontSize = 16.sp
                        ),
                        color = Color.DarkGray,
                    )
                    Box {
                        IconButton(
                            onClick = {
                                onFavoriteClick()
                                if(!isFavorite){
                                    showSmallFavorites = true
                                }
                            }
                        ) {
                            Icon(
                                imageVector = if (isFavorite) Icons.Filled.Favorite else Icons.Filled.FavoriteBorder,
                                contentDescription = if (isFavorite) "Remove from favorites" else "Add to favorites"
                            )
                        }
                        if (showSmallFavorites) {
                            SmallFavoritesExplosion {
                                showSmallFavorites = false
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun SmallFavoritesExplosion(onAnimationEnd: () -> Unit) {
    val random = Random.Default
    val favoriteCount = 10
    val targetOffsets = remember {
        List(favoriteCount) { Offset(random.nextFloat() * 200 - 100, random.nextFloat() * 200 - 100) }
    }

    val animatedOffsets = remember {
        List(favoriteCount) { Animatable(Offset(0f, 0f), Offset.VectorConverter) }
    }

    val alpha = remember { Animatable(1f) }

    LaunchedEffect(Unit) {
        targetOffsets.forEachIndexed { index, targetOffset ->
            launch {
                animatedOffsets[index].animateTo(
                    targetValue = targetOffset,
                    animationSpec = tween(durationMillis = 600, easing = FastOutSlowInEasing)
                )
            }
        }
        alpha.animateTo(
            targetValue = 0f,
            animationSpec = tween(durationMillis = 600, easing = FastOutSlowInEasing)
        )
        delay(600)
        onAnimationEnd()
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
    ) {
        animatedOffsets.forEach { animatedOffset ->
            Icon(
                imageVector = Icons.Filled.Favorite,
                contentDescription = null,
                modifier = Modifier
                    .offset(animatedOffset.value.x.dp, animatedOffset.value.y.dp)
                    .alpha(alpha.value)
                    .size(24.dp),
                tint = Color.Red.copy(alpha = alpha.value)
            )
        }
    }
}
@Preview
@Composable
fun MovieCardItemPreview() {
    MovieCardItem(
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
        )
    )
}