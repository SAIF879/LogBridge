package com.example.logbridge.ui.screens.logPicker


import android.net.Uri
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.currentOrThrow
import com.example.logbridge.ui.composables.FilePickerButton
import com.example.logbridge.ui.composables.LogPickerTopAppBar
import com.example.logbridge.ui.screens.logDetails.LogDetailsScreen
import com.example.logbridge.LogProcessor
import com.example.logbridge.utils.utiltyAndExtentions.getFileNameFromUri
import timber.log.Timber

object LogPickerScreen : Screen {

    private fun readResolve(): Any = LogPickerScreen

    @Composable
    override fun Content() {
        val context = LocalContext.current
        val navigator = LocalNavigator.currentOrThrow

//        val filePickerLauncher = rememberLauncherForActivityResult(
//            ActivityResultContracts.OpenDocument()
//        ) { uri: Uri? ->
//            uri?.let {
//                val fileName = getFileNameFromUri(context, it)
//                if (fileName?.endsWith(".txt") == true) {
//                    val inputStream = context.contentResolver.openInputStream(it)
//                    val text = inputStream?.bufferedReader().use { reader -> reader?.readText() }
//
//                    Timber.d("Contents of $fileName:\n$text")
//
//                    navigator.push(LogDetailsScreen)
//                } else {
//                    Toast.makeText(context, "Please select a .txt file", Toast.LENGTH_SHORT).show()
//                }
//            }
//        }

        val filePickerLauncher = rememberLauncherForActivityResult(
            ActivityResultContracts.OpenDocument()
        ) { uri: Uri? ->
            uri?.let {
                val fileName = getFileNameFromUri(context, it)
                if (fileName?.endsWith(".txt") == true) {
                    val inputStream = context.contentResolver.openInputStream(it)
                    val text = inputStream?.bufferedReader().use { reader -> reader?.readText() }

                    Timber.d("Contents of $fileName:\n$text")

                    // 🧠 NEW: Call Rust from Kotlin
                    val result = text?.let { content ->
                        try {
                            LogProcessor.processLog(content)
                        } catch (e: Exception) {
                            Timber.e(e, "Rust call failed")
                            "Rust processing failed"
                        }
                    }

                    Timber.d("Processed by Rust:\n$result")

                  //  navigator.push(LogDetailsScreen) // Optional: pass result if needed
                    navigator.push(LogDetailsScreen(result ?: "No result"))

                } else {
                    Toast.makeText(context, "Please select a .txt file", Toast.LENGTH_SHORT).show()
                }
            }
        }


        Scaffold(
            topBar = { LogPickerTopAppBar() },
            containerColor = Color(0xFF121416) // Dark background
        ) { paddingValues ->
            Column(
                modifier = Modifier
                    .padding(paddingValues)
                    .fillMaxSize(),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = androidx.compose.ui.Alignment.CenterHorizontally
            ) {
                Box(modifier = Modifier.weight(1f).padding(bottom = 20.dp)) // replace with lazy column for the saveed files output via outbox
                FilePickerButton {
                    filePickerLauncher.launch(arrayOf("text/plain"))
                }
            }
        }
    }
}

