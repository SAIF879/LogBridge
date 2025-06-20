package com.example.logbridge.ui.screens.logPicker

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.currentOrThrow
import com.example.logbridge.ui.screens.logDetails.LogDetailsScreen

object LogPickerScreen : Screen{

    private fun readResolve(): Any = LogPickerScreen

    @Composable
    override fun Content() {
        val navigator = LocalNavigator.currentOrThrow
      Scaffold(
          topBar = {
              Row(modifier = Modifier.fillMaxWidth() , horizontalArrangement = Arrangement.Center) {
                  Text(text = "Log Picker")
              }
          }
      ) { paddingValues ->
          Column(modifier = Modifier.padding(paddingValues).fillMaxSize() , verticalArrangement = Arrangement.Center ,) {
              Box(
                  modifier = Modifier.size(100.dp).clip(RoundedCornerShape(10.dp)).clickable{
                      // step 1 -> opens file picker and gets the log files
                      //step2 -> navigates to log details screen!
                      navigator.push(LogDetailsScreen)

                  }
              ){
                    Text("open log files !")
              }
          }
      }
    }

}