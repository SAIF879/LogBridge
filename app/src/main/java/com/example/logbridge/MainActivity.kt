 package com.example.logbridge

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import cafe.adriel.voyager.navigator.Navigator
import com.example.logbridge.ui.screens.logPicker.LogPickerScreen
import com.example.logbridge.ui.theme.LogBridgeTheme

 class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            LogBridgeTheme {
                    Navigator(LogPickerScreen)
            }
        }
    }
}

