package com.example.logbridge.ui.screens.logPicker.util

import cafe.adriel.voyager.core.model.screenModelScope
import cafe.adriel.voyager.core.model.StateScreenModel
import com.example.logbridge.data.local.LocalEntries
import io.objectbox.Box
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

// Sealed UI State
sealed class LogPickerUiState {
    object Loading : LogPickerUiState()
    data class Success(val entries: List<LocalEntries>) : LogPickerUiState()
    data class Error(val message: String) : LogPickerUiState()
}


class LogPickerScreenModel(
    private val localEntriesBox: Box<LocalEntries>
) : StateScreenModel<LogPickerUiState>(LogPickerUiState.Loading) {

    init {
        loadEntries()
    }

    fun loadEntries() {
        mutableState.value = LogPickerUiState.Loading
        screenModelScope.launch(Dispatchers.IO) {
            runCatching {
                localEntriesBox.all
            }.onSuccess { list ->
                mutableState.value = LogPickerUiState.Success(list)
            }.onFailure { error ->
                mutableState.value = LogPickerUiState.Error(error.localizedMessage ?: "Unknown error")
            }
        }
    }
}

