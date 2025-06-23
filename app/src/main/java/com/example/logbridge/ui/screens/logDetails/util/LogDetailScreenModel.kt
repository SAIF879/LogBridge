package com.example.logbridge.ui.screens.logDetails.util

import android.annotation.SuppressLint
import androidx.compose.runtime.mutableStateOf
import cafe.adriel.voyager.core.model.StateScreenModel
import cafe.adriel.voyager.core.model.screenModelScope
import com.example.logbridge.data.local.LocalEntries
import io.objectbox.Box
import kotlinx.coroutines.launch
import timber.log.Timber

data class LogDetailUiState(
    val isSaving: Boolean = false,
    val saveSuccess: Boolean? = null,
    val errorMessage: String? = null
)

class LogDetailScreenModel(
    private val localEntriesBox: Box<LocalEntries>,
) : StateScreenModel<LogDetailUiState>(LogDetailUiState()) {

    fun saveLogDetails(entry: LocalEntries) {
        screenModelScope.launch {
            mutableState.value = mutableState.value.copy(isSaving = true)

            runCatching {
                localEntriesBox.put(entry)
            }.onSuccess {
                mutableState.value = mutableState.value.copy(
                    isSaving = false,
                    saveSuccess = true
                )
                Timber.d("Log Details Saved $entry")
            }.onFailure { error ->
                mutableState.value = mutableState.value.copy(
                    isSaving = false,
                    saveSuccess = false,
                    errorMessage = error.localizedMessage
                )
                Timber.d("Log Details  Not Saved $error")
            }
        }
    }
}