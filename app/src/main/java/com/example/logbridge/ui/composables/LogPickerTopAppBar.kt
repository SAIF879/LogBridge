package com.example.logbridge.ui.composables

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.ArrowBack
import androidx.compose.material.icons.rounded.ArrowBack
import androidx.compose.material.icons.rounded.Menu
import androidx.compose.material.icons.rounded.Save
import androidx.compose.material.icons.rounded.Settings
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LogPickerTopAppBar(
    heading : String  ,
    onPrimaryAction: () -> Unit = {},
    onSecondaryAction: () -> Unit = {},
    isDetailMode : Boolean = false,
    hideSecondaryAction : Boolean = false
) {
    TopAppBar(
        title = {
            Text(
                text = heading,
                style = MaterialTheme.typography.titleLarge.copy(
                    fontWeight = FontWeight.SemiBold,
                    letterSpacing = 0.5.sp
                ),
                color = Color(0xFFE0E0E0)
            )
        },
        navigationIcon = {
            Icon(
                imageVector = if (isDetailMode) Icons.AutoMirrored.Rounded.ArrowBack
                              else Icons.Rounded.Menu,
                contentDescription = if (isDetailMode) "Arrow_back" else "Menu",
                modifier = Modifier
                    .padding(start = 16.dp)
                    .clickable { onPrimaryAction() },
                tint = Color(0xFF4DA6FF)
            )
        },
        actions = {
            if (!hideSecondaryAction) {
                Icon(
                    imageVector = if (isDetailMode) Icons.Rounded.Save else Icons.Rounded.Settings,
                    contentDescription = "Settings",
                    modifier = Modifier
                        .padding(end = 16.dp)
                        .clickable { onSecondaryAction() },
                    tint = Color(0xFFA0A0A0)
                )
            }
        },
        modifier = Modifier
            .shadow(elevation = 8.dp, shape = RectangleShape)
            .background(
                brush = Brush.verticalGradient(
                    colors = listOf(Color(0xFF1A1C1E), Color(0xFF121416))
                )
            ),
        colors = TopAppBarDefaults.topAppBarColors(
            containerColor = Color.Transparent
        )
    )
}
