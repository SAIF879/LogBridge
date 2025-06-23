package com.example.logbridge.ui.screens.logPicker.ui

import android.net.Uri
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.koin.koinScreenModel
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.currentOrThrow
import com.example.logbridge.LogProcessor
import com.example.logbridge.data.local.LocalEntries
import com.example.logbridge.ui.composables.FilePickerButton
import com.example.logbridge.ui.composables.LocalEntryCard
import com.example.logbridge.ui.composables.LogPickerTopAppBar
import com.example.logbridge.ui.screens.logDetails.ui.LogDetailsScreen
import com.example.logbridge.ui.screens.logPicker.util.LogPickerScreenModel
import com.example.logbridge.ui.screens.logPicker.util.LogPickerUiState
import com.example.logbridge.ui.screens.settings.ui.SettingsScreen
import com.example.logbridge.utils.utiltyAndExtentions.getFileNameFromUri
import timber.log.Timber

object LogPickerScreen : Screen {

    private fun readResolve(): Any = LogPickerScreen

    @Composable
    override fun Content() {
        val context = LocalContext.current
        val navigator = LocalNavigator.currentOrThrow
        val screenmodel = koinScreenModel<LogPickerScreenModel>()
        val uiState = screenmodel.state.collectAsState().value
        val filePickerLauncher = rememberLauncherForActivityResult(
            ActivityResultContracts.OpenDocument()
        ) { uri: Uri? ->
            uri?.let {
                val fileName = getFileNameFromUri(context, it)
                if (fileName?.endsWith(".txt") == true) {
                    val inputStream = context.contentResolver.openInputStream(it)
                    val text = inputStream?.bufferedReader().use { reader -> reader?.readText() }
                    Timber.Forest.d("Contents of $fileName:\n$text")
                    val result = text?.let { content ->
                        try {
                            LogProcessor.processLog(content)
                        } catch (e: Exception) {
                            Timber.Forest.e(e, "Rust call failed")
                            "Rust processing failed"
                        }
                    }

                    Timber.Forest.d("Processed by Rust:\n$result")
                    navigator.push(LogDetailsScreen(result ?: "No result" , fileName = fileName))

                } else {
                    Toast.makeText(context, "Please select a .txt file", Toast.LENGTH_SHORT).show()
                }
            }
        }


        Scaffold(
            topBar = { LogPickerTopAppBar(heading = "LogBridge" , onSecondaryAction = {
                navigator.push(SettingsScreen)
            }) },
            containerColor = Color(0xFF121416)
        ) { paddingValues ->
            Column(
                modifier = Modifier.Companion
                    .padding(paddingValues)
                    .fillMaxSize(),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Box(
                    modifier = Modifier.Companion.weight(1f).padding(bottom = 20.dp)
                ) {
                  when(uiState){
                      is LogPickerUiState.Error -> {

                      }
                      LogPickerUiState.Loading -> {
                          LoadingIndicator()
                      }
                      is LogPickerUiState.Success -> {
                          EntriesList(entries = uiState.entries, onClick = {
                              navigator.push(LogDetailsScreen(it.filePath, fileName = it.fileName))
                          })
                      }
                  }
                }
                FilePickerButton {
                    filePickerLauncher.launch(arrayOf("text/plain"))
                }
            }
        }
    }
}
@Composable
private fun LoadingIndicator() {
    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        CircularProgressIndicator(
            modifier = Modifier.size(48.dp),
            strokeWidth = 3.dp,
            color = MaterialTheme.colorScheme.primary
        )
    }
}

@Composable
fun EntriesList(entries: List<LocalEntries>, onClick: (LocalEntries) -> Unit) {
    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        item { Spacer(modifier = Modifier.height(8.dp)) }

        items(
            items  = entries,
            key = { it.id }
        ) { entry ->

            AnimatedVisibility(
                visible = true,
                enter = fadeIn(tween(300)),
                exit = fadeOut(tween(150))
            ) {
                LocalEntryCard(
                    entry = entry,
                    onClick = { onClick(entry) },
                    modifier = Modifier
                        .fillMaxWidth()
                )
            }
        }

        item { Spacer(modifier = Modifier.height(80.dp)) }
    }
}
