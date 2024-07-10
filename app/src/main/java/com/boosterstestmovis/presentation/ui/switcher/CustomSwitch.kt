package com.boosterstestmovis.presentation.ui.switcher

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

@Composable
fun CustomSwitch(
    isChecked: Boolean,
    onCheckedChange: (Boolean) -> Unit
) {
    Row(
        Modifier
            .padding(8.dp)
            .background(Color.White, shape = RoundedCornerShape(16.dp))
            .border(1.dp, Color.Gray, shape = RoundedCornerShape(16.dp))
            .clickable(
                indication = null, // Disable the ripple effect
                interactionSource = remember { MutableInteractionSource() }
            ) { onCheckedChange(!isChecked) }
            .padding(4.dp)
    ) {
        val modifier = Modifier
            .weight(1f)
            .height(40.dp)
            .clip(RoundedCornerShape(16.dp))

        Box(
            modifier = modifier
                .background(if (isChecked) Color(0xFFB0C4DE) else Color.Transparent)
                .clickable(
                    indication = null, // Disable the ripple effect
                    interactionSource = remember { MutableInteractionSource() }
                ) { if (!isChecked) onCheckedChange(true) },
            contentAlignment = Alignment.Center
        ) {
            Text(text = "Movies", color = if (isChecked) Color.Blue else Color.Black)
        }

        Spacer(modifier = Modifier.width(8.dp))

        Box(
            modifier = modifier
                .background(if (!isChecked) Color(0xFFB0C4DE) else Color.Transparent)
                .clickable(
                    indication = null, // Disable the ripple effect
                    interactionSource = remember { MutableInteractionSource() }
                ) { if (isChecked) onCheckedChange(false) },
            contentAlignment = Alignment.Center
        ) {
            Text(text = "Favourites", color = if (!isChecked) Color.Blue else Color.Black)
        }
    }
}