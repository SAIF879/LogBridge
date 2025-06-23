 package com.example.logbridge

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import cafe.adriel.voyager.navigator.Navigator
import com.example.logbridge.ui.screens.logDetails.LogDetailsScreen
import com.example.logbridge.ui.screens.logPicker.LogPickerScreen
import com.example.logbridge.ui.theme.LogBridgeTheme
import timber.log.Timber

 class MainActivity : ComponentActivity() {
     override fun onCreate(savedInstanceState: Bundle?) {
         super.onCreate(savedInstanceState)
         enableEdgeToEdge()

         val sharedText = handleSharedTextFile(intent)

         setContent {
             LogBridgeTheme {
                 Navigator(
                     if (sharedText != null) {
                         val result = try {
                             LogProcessor.processLog(sharedText)
                         } catch (e: Exception) {
                             "Error: ${e.message}"
                         }
                         LogDetailsScreen(result)
                     } else {
                         LogPickerScreen
                     }
                 )
             }
         }
     }

     override fun onNewIntent(intent: Intent) {
         super.onNewIntent(intent)
         // Optional: if you want to handle new files while app is already open
         // You'll need a shared ViewModel or Navigator ref to push new screen
     }

     private fun handleSharedTextFile(intent: Intent?): String? {
         val uri = intent?.data ?: return null
         return try {
             contentResolver.openInputStream(uri)?.bufferedReader()?.use { it.readText() }
         } catch (e: Exception) {
             Timber.e(e, "Error reading shared .txt file")
             null
         }
     }
 }
