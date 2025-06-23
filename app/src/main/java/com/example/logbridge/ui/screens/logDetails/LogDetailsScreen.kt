package com.example.logbridge.ui.screens.logDetails

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import cafe.adriel.voyager.core.screen.Screen
import com.example.logbridge.data.LogEntry
import com.example.logbridge.ui.composables.FilePickerButton
import com.example.logbridge.ui.composables.LogPickerTopAppBar
import com.example.logbridge.ui.theme.*
import com.google.gson.Gson
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

data class LogDetailsScreen(val result: String) : Screen {

    @OptIn(ExperimentalFoundationApi::class)
    @Composable
    override fun Content() {
        val logEntries: List<LogEntry> = remember(result) {
            try {
                Gson().fromJson(result, Array<LogEntry>::class.java).toList()
            } catch (e: Exception) {
                emptyList()
            }
        }

        var searchQuery by remember { mutableStateOf("") }
        var selectedLevel by remember { mutableStateOf("ALL") }
        val context = LocalContext.current

        val filteredEntries = logEntries.filter { entry ->
            (selectedLevel == "ALL" || entry.level == selectedLevel) &&
                    (searchQuery.isEmpty() ||
                            entry.message.contains(searchQuery, ignoreCase = true) ||
                            entry.module?.contains(searchQuery, ignoreCase = true) == true)
        }

        Scaffold(
            topBar = {
                LogPickerTopAppBar(

                )
            },
            containerColor = MaterialTheme.colorScheme.background,
        ) { paddingValues ->
            Column(
                modifier = Modifier
                    .padding(paddingValues)
                    .fillMaxSize()
            ) {
                // Filter controls
                LogFilterBar(
                    searchQuery = searchQuery,
                    onSearchChange = { searchQuery = it },
                    selectedLevel = selectedLevel,
                    onLevelSelected = { selectedLevel = it },
                    modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)
                )

                if (filteredEntries.isEmpty()) {
                    EmptyLogState()
                } else {
                    LogTimeline(
                        entries = filteredEntries,
                        modifier = Modifier.padding(top = 8.dp)
                    )
                }
            }
        }
    }
}

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun LogFilterBar(
    searchQuery: String,
    onSearchChange: (String) -> Unit,
    selectedLevel: String,
    onLevelSelected: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    val logLevels = listOf("ALL", "ERROR", "WARN", "INFO", "DEBUG")

    Column(modifier = modifier) {
        // Search bar
        OutlinedTextField(
            value = searchQuery,
            onValueChange = onSearchChange,
            leadingIcon = { Icon(Icons.Default.Search, null, tint = MaterialTheme.colorScheme.onSurfaceVariant) },
            placeholder = { Text("Search logs...", color = MaterialTheme.colorScheme.onSurfaceVariant) },
            colors = TextFieldDefaults.colors(
                focusedContainerColor = MaterialTheme.colorScheme.surfaceContainerLow,
                unfocusedContainerColor = MaterialTheme.colorScheme.surfaceContainerLow,
                focusedIndicatorColor = MaterialTheme.colorScheme.primary,
                unfocusedIndicatorColor = MaterialTheme.colorScheme.outlineVariant
            ),
            shape = RoundedCornerShape(12.dp),
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(16.dp))

        // Level filter chips
        Text(
            text = "Log Level",
            style = MaterialTheme.typography.labelMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            modifier = Modifier.padding(bottom = 8.dp)
        )

        FlowRow(
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            logLevels.forEach { level ->
                val isSelected = level == selectedLevel
                val (containerColor, contentColor) = when(level) {
                    "ERROR" -> Pair(MaterialTheme.colorScheme.errorContainer, MaterialTheme.colorScheme.onErrorContainer)
                    "WARN" -> Pair(MaterialTheme.colorScheme.tertiaryContainer, MaterialTheme.colorScheme.onTertiaryContainer)
                    "INFO" -> Pair(MaterialTheme.colorScheme.secondaryContainer, MaterialTheme.colorScheme.onSecondaryContainer)
                    "DEBUG" -> Pair(MaterialTheme.colorScheme.primaryContainer, MaterialTheme.colorScheme.onPrimaryContainer)
                    else -> Pair(MaterialTheme.colorScheme.surfaceContainerHighest, MaterialTheme.colorScheme.onSurfaceVariant)
                }

                FilterChip(
                    selected = isSelected,
                    onClick = { onLevelSelected(level) },
                    label = {
                        Text(
                            text = level,
                            style = MaterialTheme.typography.labelMedium,
                            fontWeight = FontWeight.Medium
                        )
                    },
                    leadingIcon = {
                        if (isSelected) Icon(Icons.Default.Check, null, tint = contentColor)
                    },
                    colors = FilterChipDefaults.filterChipColors(
                        selectedContainerColor = containerColor,
                        selectedLabelColor = contentColor,
                        containerColor = MaterialTheme.colorScheme.surfaceContainerHigh,
                        labelColor = MaterialTheme.colorScheme.onSurface
                    ),
                    border = FilterChipDefaults.filterChipBorder(
                        enabled = true,
                        borderColor = MaterialTheme.colorScheme.outlineVariant,
                        selectedBorderColor = MaterialTheme.colorScheme.outline,
                        selected =isSelected,
                    )
                )
            }
        }
    }
}

@Composable
fun LogTimeline(entries: List<LogEntry>, modifier: Modifier = Modifier) {
    LazyColumn(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(12.dp),
        contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp)
    ) {
        items(entries) { entry ->
            LogEntryCard(entry)
        }
    }
}

@Composable
fun LogEntryCard(entry: LogEntry) {
    val levelColors = when (entry.level) {
        "ERROR" -> LevelColors(
            container = MaterialTheme.colorScheme.errorContainer,
            content = MaterialTheme.colorScheme.onErrorContainer,
            icon = Icons.Default.Error
        )
        "WARN" -> LevelColors(
            container = MaterialTheme.colorScheme.tertiaryContainer,
            content = MaterialTheme.colorScheme.onTertiaryContainer,
            icon = Icons.Default.Warning
        )
        "INFO" -> LevelColors(
            container = MaterialTheme.colorScheme.secondaryContainer,
            content = MaterialTheme.colorScheme.onSecondaryContainer,
            icon = Icons.Default.Info
        )
        "DEBUG" -> LevelColors(
            container = MaterialTheme.colorScheme.primaryContainer,
            content = MaterialTheme.colorScheme.onPrimaryContainer,
            icon = Icons.Default.BugReport
        )
        else -> LevelColors(
            container = MaterialTheme.colorScheme.surfaceContainerHigh,
            content = MaterialTheme.colorScheme.onSurface,
            icon = Icons.Default.QuestionMark
        )
    }

    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceContainerHigh
        ),
        elevation = CardDefaults.cardElevation(2.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            // Header with level and timestamp
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.fillMaxWidth()
            ) {
                // Level indicator
                Box(
                    modifier = Modifier
                        .size(32.dp)
                        .clip(CircleShape)
                        .background(levelColors.container)
                        .padding(6.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = levelColors.icon,
                        contentDescription = entry.level,
                        tint = levelColors.content,
                        modifier = Modifier.size(18.dp)
                    )
                }

                Spacer(modifier = Modifier.width(12.dp))

                Column {
                    Text(
                        text = entry.level ?: "UNKNOWN",
                        style = MaterialTheme.typography.labelLarge,
                        fontWeight = FontWeight.SemiBold,
                        color = levelColors.content
                    )

                    Text(
                        text = entry.timestamp?.formatTimestamp() ?: "No timestamp",
                        style = MaterialTheme.typography.labelSmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }

                Spacer(modifier = Modifier.weight(1f))

                // Metadata chips
                Row(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                    entry.pid?.let {
                        MetadataChip("PID: $it")
                    }
                    entry.thread?.let {
                        MetadataChip("Thread: $it")
                    }
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            // Module and message
            entry.module?.let {
                Text(
                    text = it,
                    style = MaterialTheme.typography.bodyMedium,
                    fontWeight = FontWeight.Medium,
                    color = MaterialTheme.colorScheme.primary,
                    modifier = Modifier.padding(bottom = 4.dp)
                )
            }

            Text(
                text = entry.message,
                style = MaterialTheme.typography.bodyLarge,
                color = MaterialTheme.colorScheme.onSurface,
                modifier = Modifier.padding(bottom = 12.dp)
            )

            // Error code
            entry.error_code?.let {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier
                        .clip(RoundedCornerShape(8.dp))
                        .background(MaterialTheme.colorScheme.error.copy(alpha = 0.1f))
                        .padding(horizontal = 12.dp, vertical = 6.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.ErrorOutline,
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.error,
                        modifier = Modifier.size(16.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = "Error Code: $it",
                        style = MaterialTheme.typography.bodyMedium,
                        fontWeight = FontWeight.Medium,
                        color = MaterialTheme.colorScheme.error
                    )
                }
            }
        }
    }
}

@Composable
fun MetadataChip(text: String) {
    Box(
        modifier = Modifier
            .clip(RoundedCornerShape(8.dp))
            .background(MaterialTheme.colorScheme.surfaceContainerHighest)
            .padding(horizontal = 8.dp, vertical = 4.dp)
    ) {
        Text(
            text = text,
            style = MaterialTheme.typography.labelSmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
    }
}

@Composable
fun EmptyLogState() {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.padding(32.dp)
        ) {
            Icon(
                imageVector = Icons.Default.FilterList,
                contentDescription = null,
                tint = MaterialTheme.colorScheme.onSurfaceVariant,
                modifier = Modifier.size(64.dp)
            )
            Spacer(modifier = Modifier.height(16.dp))
            Text(
                text = "No matching logs found",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Medium,
                color = MaterialTheme.colorScheme.onSurface
            )
            Text(
                text = "Try adjusting your filters or search query",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                modifier = Modifier.padding(top = 8.dp)
            )
        }
    }
}

// Extension to format timestamp
fun String.formatTimestamp(): String {
    return try {
        val formatter = DateTimeFormatter.ofPattern("MMM dd, HH:mm:ss")
        val parsed = LocalDateTime.parse(this, DateTimeFormatter.ISO_DATE_TIME)
        parsed.format(formatter)
    } catch (e: Exception) {
        this
    }
}

data class LevelColors(
    val container: Color,
    val content: Color,
    val icon: ImageVector
)