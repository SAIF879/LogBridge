package com.example.logbridge.ui.screens.settings.util

import cafe.adriel.voyager.core.model.StateScreenModel
import cafe.adriel.voyager.core.model.screenModelScope
import com.example.logbridge.data.local.LocalEntries
import io.objectbox.Box
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch


sealed class SettingsUiState {
    object Loading : SettingsUiState()
    data class Success(val entries: List<LocalEntries>) : SettingsUiState()
    data class Error(val message: String) : SettingsUiState()
}

class SettingsScreenModel(
    private val localEntriesBox: Box<LocalEntries>
) : StateScreenModel<SettingsUiState>(SettingsUiState.Success(emptyList())) {

    fun clearEntries() {
        mutableState.value = SettingsUiState.Loading
        screenModelScope.launch(Dispatchers.IO) {
            runCatching {
                localEntriesBox.removeAll()
            }.onSuccess {
                mutableState.value = SettingsUiState.Success(emptyList())
            }.onFailure { e ->
                mutableState.value = SettingsUiState.Error(e.localizedMessage ?: "Failed to clear entries")
            }
        }
    }
}
