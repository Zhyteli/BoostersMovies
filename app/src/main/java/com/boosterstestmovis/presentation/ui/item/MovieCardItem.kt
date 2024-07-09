package com.boosterstestmovis.presentation.ui.item

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
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
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImagePainter
import coil.compose.rememberAsyncImagePainter
import com.boosterstestmovis.presentation.ui.item.ImageLink.BASE_POSTER_URL
import com.boosterstestmovis.presentation.ui.item.ImageLink.SMALL_POSTER_SIZE
import com.boosterstestmovis.presentation.ui.item.anim.LoadingImageAnim

@Composable
fun MovieCardItem(
    posterPath: String,
    movieName: String,
    description: String,
    rating: Double
) {
    val imageUrl = BASE_POSTER_URL + SMALL_POSTER_SIZE + posterPath

    var loading by remember { mutableStateOf(true) }
    val painter = rememberAsyncImagePainter(imageUrl)

    LaunchedEffect(painter) {
        snapshotFlow { painter.state }
            .collect { state ->
                loading = state is AsyncImagePainter.State.Loading
            }
    }

    MovieCard(loading, painter, movieName, description, rating)
}

@Composable
private fun MovieCard(
    loading: Boolean,
    painter: AsyncImagePainter,
    movieName: String,
    description: String,
    rating: Double
) {
    Card(
        modifier = Modifier
            .padding(16.dp)
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
                Spacer(modifier = Modifier.height(16.dp))
                Text(
                    text = "Rating: $rating",
                    style = MaterialTheme.typography.bodyMedium.copy(
                        fontSize = 16.sp
                    ),
                    color = Color.DarkGray,
                )

            }
        }
    }
}

@Preview
@Composable
fun MovieCardItemPreview() {
    MovieCardItem(
        posterPath = "/k1VK2L971GmEevIjFbjcimxSeMx.jpg", // Replace with your image resource
        movieName = "Movie Title",
        description = "This is a short description of the movie. It should give an overview of the plot or theme.",
        rating = 4.5
    )
}