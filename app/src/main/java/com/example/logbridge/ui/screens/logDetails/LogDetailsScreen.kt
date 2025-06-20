package com.example.logbridge.ui.screens.logDetails

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import cafe.adriel.voyager.core.screen.Screen

object LogDetailsScreen : Screen{
    private fun readResolve(): Any = LogDetailsScreen

    @Composable
    override fun Content() {
            LazyColumn(modifier = Modifier.fillMaxSize()) {
             item {    Text("log details screen") }
            }
    }

}