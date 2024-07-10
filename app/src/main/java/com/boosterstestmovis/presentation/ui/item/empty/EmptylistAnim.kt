package com.boosterstestmovis.presentation.ui.item.empty

import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

@Composable
fun EmptyListAnim() {
    val list = arrayOfNulls<Int>(6)
    Box(modifier = Modifier
        .fillMaxSize()
        .background(Color(0xFFFFE3CA))) {
        LazyColumn(Modifier.align(Alignment.Center)) {
            items(list) {
                EmptyItem()
            }
        }
    }
}

@Composable
fun EmptyItem() {
    val infiniteTransition = rememberInfiniteTransition()
    val animatedOffsetX by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 1000f,
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = 2000, easing = LinearEasing),
            repeatMode = RepeatMode.Reverse
        ), label = "loading animation"
    )

    val gradient = Brush.linearGradient(
        colors = listOf(
            Color(0xFFF3D7D7),
            Color(0xFFCEC3C3),
            Color(0xFFF3D7D7)
        ),
        start = Offset(animatedOffsetX, 0f),
        end = Offset(animatedOffsetX + 350f, 200f)
    )

    Card(
        modifier = Modifier
            .padding(16.dp)
            .size(350.dp, 200.dp)
            .clip(RoundedCornerShape(16.dp)),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(
            containerColor = Color.Transparent
        )
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(gradient)
        )
    }
}


@Preview
@Composable
fun EmptyListAnimPreview() {
    EmptyListAnim()
}