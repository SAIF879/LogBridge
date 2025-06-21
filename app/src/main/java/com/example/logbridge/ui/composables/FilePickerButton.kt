package com.example.logbridge.ui.composables

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.ripple.rememberRipple
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.logbridge.R

@Composable
fun FilePickerButton(onClick: () -> Unit) {
    val interactionSource = remember { MutableInteractionSource() }
    val borderColor = if (isSystemInDarkTheme()) Color(0xFF6C6C6C) else Color(0xFFB0B0B0)

    Card(
        modifier = Modifier.run {
            fillMaxWidth()
                .height(120.dp)
                .padding(horizontal = 24.dp, vertical = 12.dp)
                .clickable(
                    interactionSource = interactionSource,
                    indication = null,
                    onClick = onClick
                )
        },
        shape = RoundedCornerShape(16.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 8.dp),
        colors = CardDefaults.cardColors(
            containerColor = Color(0xFF1A1C1E),
            contentColor = Color.White
        ),
        border = BorderStroke(width = 1.dp, color = borderColor.copy(alpha = 0.3f))
    ) {
        Row(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 24.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Column(
                verticalArrangement = Arrangement.Center,
                modifier = Modifier.weight(1f)
            ) {
                Text(
                    text = "Select Log File",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.SemiBold
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = "Click to browse your files",
                    style = MaterialTheme.typography.bodySmall,
                    color = Color(0xFFA0A0A0)
                )
            }

            Box(
                modifier = Modifier
                    .size(80.dp)
                    .background(
                        color = Color(0xFF2C2F33),
                        shape = CircleShape
                    )
                    .padding(12.dp)
            ) {
            LottieAnimation(R.raw.file, size = 80.dp , modifier = Modifier
                .align(Alignment.Center)
                .padding(10.dp))
            }
        }
    }
}