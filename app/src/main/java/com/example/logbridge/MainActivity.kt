 package com.example.logbridge

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import cafe.adriel.voyager.navigator.Navigator
import com.example.logbridge.ui.screens.logDetails.ui.LogDetailsScreen
import com.example.logbridge.ui.screens.logPicker.ui.LogPickerScreen
import com.example.logbridge.ui.theme.LogBridgeTheme
import com.example.logbridge.utils.utiltyAndExtentions.getFileNameFromUri
import timber.log.Timber

 class MainActivity : ComponentActivity() {
     override fun onCreate(savedInstanceState: Bundle?) {
         super.onCreate(savedInstanceState)

         enableEdgeToEdge()
         val sharedData = handleSharedTextFile(intent)
         setContent {
             LogBridgeTheme {
                 Navigator(
                     if (sharedData != null) {
                         val (fileName, textContent) = sharedData
                         val result = try {
                             LogProcessor.processLog(textContent)
                         } catch (e: Exception) {
                             "Error: ${e.message}"
                         }
                         LogDetailsScreen(result = result, fileName = fileName)
                     } else {
                         LogPickerScreen
                     }
                 )
             }
         }
     }

     private fun handleSharedTextFile(intent: Intent?): Pair<String, String>? {
         val uri = intent?.data ?: return null

         return runCatching {
             val fileName = getFileNameFromUri(this, uri) ?: "shared_file.txt"
             val content = contentResolver.openInputStream(uri)
                 ?.bufferedReader()
                 ?.use { it.readText() }
             if (content.isNullOrEmpty()) null else fileName to content
         }.onFailure {
             Timber.e(it, "Error reading shared file")
         }.getOrNull()
     }

 }
