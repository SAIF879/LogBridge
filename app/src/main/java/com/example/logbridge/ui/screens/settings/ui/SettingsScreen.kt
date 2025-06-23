package com.example.logbridge.ui.screens.settings.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.DeleteForever
import androidx.compose.material.icons.filled.NightsStay
import androidx.compose.material.icons.filled.Palette
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Surface
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.koin.koinScreenModel
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.currentOrThrow
import com.example.logbridge.R
import com.example.logbridge.ui.composables.LottieAnimation
import com.example.logbridge.ui.screens.settings.util.SettingsScreenModel

@OptIn(ExperimentalMaterial3Api::class)
object SettingsScreen : Screen {

    @Composable
    override fun Content() {
        val navigator = LocalNavigator.currentOrThrow
        val screenModel = koinScreenModel<SettingsScreenModel>()
        val snackbarHostState = remember { SnackbarHostState() }
        var showClearConfirmation by rememberSaveable { mutableStateOf(false) }
        var showSuccessMessage by rememberSaveable { mutableStateOf(false) }

        // Handle clear operation result
        if (showSuccessMessage) {
            LaunchedEffect(showSuccessMessage) {
                snackbarHostState.showSnackbar("All data cleared successfully")
                showSuccessMessage = false
            }
        }

        // Handle confirmation result
        if (showClearConfirmation) {
            ClearConfirmationDialog(
                onConfirm = {
                    screenModel.clearEntries()
                    showClearConfirmation = false
                    showSuccessMessage = true
                },
                onDismiss = { showClearConfirmation = false }
            )
        }

        Scaffold(
            snackbarHost = { SnackbarHost(snackbarHostState) },
            topBar = {
                TopAppBar(
                    title = {
                        Text(
                            text = "Settings",
                            fontWeight = FontWeight.SemiBold,
                            fontSize = 20.sp,
                            color = Color(0xFFE0E0E0)
                        )
                    },
                    navigationIcon = {
                        IconButton(onClick = { navigator.pop() }) {
                            Icon(
                                imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                                contentDescription = "Back",
                                tint = Color(0xFF64B5F6)
                            )
                        }
                    },
                    colors = TopAppBarDefaults.topAppBarColors(
                        containerColor = Color(0xFF121416),
                        titleContentColor = MaterialTheme.colorScheme.onSurface
                    )
                )
            },
            containerColor = Color(0xFF0A0C0D)
        ) { paddingValues ->
            Column(
                modifier = Modifier
                    .padding(paddingValues)
                    .fillMaxSize()
                    .verticalScroll(rememberScrollState())
            ) {
                // Decorative header
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(180.dp)
                        .background(Color(0xFF121416))

                ) {

                  Row(modifier = Modifier.fillMaxWidth() , horizontalArrangement = Arrangement.Center) {
                      LottieAnimation(R.raw.file , size = 150.dp  ,)
                  }

                    Text(
                        text = "Customize Your Experience",
                        color = Color(0xFF64B5F6),
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Medium,
                        modifier = Modifier
                            .align(Alignment.BottomCenter)
                            .padding(bottom = 16.dp)
                    )
                }

                Spacer(modifier = Modifier.height(8.dp))

                // Appearance Section
                SettingsSection(title = "APPEARANCE") {
                    SettingsOptionCard(
                        icon = Icons.Filled.Palette,
                        title = "Dark Theme",
                        description = "Enable dark mode for better night viewing",
                        action = {
                            Switch(
                                checked = true,
                                onCheckedChange = {},
                                colors = SwitchDefaults.colors(
                                    checkedThumbColor = Color(0xFF64B5F6),
                                    checkedTrackColor = Color(0xFF64B5F6).copy(alpha = 0.5f),
                                    uncheckedThumbColor = Color(0xFF757575),
                                    uncheckedTrackColor = Color(0xFF757575).copy(alpha = 0.5f)
                                )
                            )
                        }
                    )

                    SettingsOptionCard(
                        icon = Icons.Filled.NightsStay,
                        title = "True Black Mode",
                        description = "Use pure black for OLED screens",
                        action = {
                            Switch(
                                checked = false,
                                onCheckedChange = {},
                                colors = SwitchDefaults.colors(
                                    checkedThumbColor = Color(0xFF64B5F6),
                                    checkedTrackColor = Color(0xFF64B5F6).copy(alpha = 0.5f),
                                    uncheckedThumbColor = Color(0xFF757575),
                                    uncheckedTrackColor = Color(0xFF757575).copy(alpha = 0.5f)
                                )
                            )
                        }
                    )
                }

                // Data Management Section
                SettingsSection(title = "DATA") {
                    SettingsOptionCard(
                        icon = Icons.Filled.DeleteForever,
                        title = "Clear All Data",
                        description = "Permanently delete all locally stored entries",
                        iconTint = Color(0xFFEF5350),
                        onClick = { showClearConfirmation = true }
                    )
                }

                // App Info Section
                SettingsSection(title = "ABOUT") {
                    Text(
                        text = "LogBridge v1.0.0",
                        color = Color(0xFFA0A0A0),
                        modifier = Modifier.padding(horizontal = 24.dp, vertical = 16.dp)
                    )

                    Text(
                        text = "© I can't do this anymore 😭",
                        color = Color(0xFF707070),
                        fontSize = 12.sp,
                        textAlign = TextAlign.Center,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(top = 32.dp, bottom = 16.dp)
                    )
                }

                Spacer(modifier = Modifier.height(32.dp))
            }
        }
    }
}

@Composable
private fun SettingsSection(
    title: String,
    content: @Composable () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp)
    ) {
        Text(
            text = title,
            color = Color(0xFF64B5F6),
            fontSize = 14.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(start = 24.dp, bottom = 8.dp)
        )
        content()
        Spacer(modifier = Modifier.height(8.dp))
        Divider(
            color = Color(0xFF2C2F33),
            thickness = 1.dp,
            modifier = Modifier.padding(horizontal = 24.dp, vertical = 8.dp)
        )
    }
}

@Composable
private fun SettingsOptionCard(
    title: String,
    description: String? = null,
    icon: ImageVector? = null,
    iconTint: Color = Color(0xFF64B5F6),
    action: @Composable (() -> Unit)? = null,
    onClick: (() -> Unit)? = null
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 20.dp, vertical = 8.dp)
            .height(if (description != null) 100.dp else 80.dp),
        shape = RoundedCornerShape(16.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 6.dp),
        colors = CardDefaults.cardColors(
            containerColor = Color(0xFF1A1C1E),
            contentColor = Color.White
        ),
        border = CardDefaults.outlinedCardBorder()
    ) {
        Surface(
            modifier = Modifier
                .fillMaxSize()
                .clickable(enabled = onClick != null, onClick = { onClick?.invoke() }),
            color = Color.Transparent
        ) {
            Row(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = 20.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Icon Container
                icon?.let {
                    Box(
                        modifier = Modifier
                            .size(50.dp)
                            .clip(CircleShape)
                            .background(Color(0xFF2C2F33))
                            .padding(12.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = icon,
                            contentDescription = null,
                            tint = iconTint,
                            modifier = Modifier.size(24.dp)
                        )
                    }
                    Spacer(modifier = Modifier.size(16.dp))
                }

                // Text Content
                Column(
                    modifier = Modifier.weight(1f),
                    verticalArrangement = Arrangement.Center
                ) {
                    Text(
                        text = title,
                        style = MaterialTheme.typography.bodyLarge,
                        fontWeight = FontWeight.Medium,
                        color = Color(0xFFE0E0E0)
                    )
                    description?.let {
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = description,
                            style = MaterialTheme.typography.bodyMedium,
                            color = Color(0xFFA0A0A0)
                        )
                    }
                }

                // Action
                action?.let {
                    Spacer(modifier = Modifier.size(16.dp))
                    Box(modifier = Modifier.size(50.dp)) {
                        action()
                    }
                }
            }
        }
    }
}

@Composable
private fun ClearConfirmationDialog(
    onConfirm:  () -> Unit,
    onDismiss: () -> Unit
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        containerColor = Color(0xFF1A1C1E),
        titleContentColor = Color(0xFFEF5350),
        textContentColor = Color(0xFFE0E0E0),
        shape = RoundedCornerShape(24.dp),
        title = {
            Text(
                text = "Confirm Deletion",
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold
            )
        },
        text = {
            Column {
                Text(
                    text = "This will permanently delete all locally stored log entries.",
                    style = MaterialTheme.typography.bodyMedium
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = "This action cannot be undone.",
                    style = MaterialTheme.typography.bodyMedium,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFFEF5350)
                )
            }
        },
        confirmButton = {
            TextButton(
                onClick = onConfirm,
                modifier = Modifier.padding(end = 8.dp)
            ) {
                Text(
                    text = "DELETE",
                    style = MaterialTheme.typography.labelLarge,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFFEF5350)
                )
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text(
                    text = "CANCEL",
                    style = MaterialTheme.typography.labelLarge,
                    fontWeight = FontWeight.Medium,
                    color = Color(0xFF64B5F6)
                )
            }
        }
    )
}