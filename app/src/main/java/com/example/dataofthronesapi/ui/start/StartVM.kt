package com.example.dataofthronesapi.ui.start

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.CreationExtras
import com.example.dataofthronesapi.data.UserPreferences
import com.example.dataofthronesapi.dependencies.DataOfThrones
import com.example.dataofthronesapi.repositories.UserPreferencesRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class StartVM(
    private val userPreferencesRepository: UserPreferencesRepository
) : ViewModel() {

    private val _uiState: MutableStateFlow<UserPreferences> = MutableStateFlow(UserPreferences())
    val uiState: StateFlow<UserPreferences> = _uiState.asStateFlow()

    fun saveSaveSettingsStart(checked: Boolean) {
        viewModelScope.launch {
            try {
                userPreferencesRepository.saveSettingsStart(checked)
                updateStateSuccess()
            } catch (e: Error) {
                updateStateError()
            }
        }
    }

    private fun updateStateSuccess() {
        _uiState.update { currentState ->
            currentState.copy(
                savedSkipIntro = true
            )

        }
    }

    private fun updateStateError() {
        _uiState.update { currentState ->
            currentState.copy(
                error = true
            )

        }
    }


    companion object {
        val Factory: ViewModelProvider.Factory = object : ViewModelProvider.Factory {
            @Suppress("UNCHECKED_CAST")
            override fun <T : ViewModel> create(
                modelClass: Class<T>,
                extras: CreationExtras
            ): T {
                val application =
                    checkNotNull(extras[ViewModelProvider.AndroidViewModelFactory.APPLICATION_KEY])

                return StartVM(
                    (application as DataOfThrones).appcontainer.userPreferencesRepository
                ) as T
            }
        }
    }

}