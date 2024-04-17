package com.example.dataofthronesapi.ui.userinfo

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.CreationExtras
import com.example.dataofthronesapi.dependencies.DataOfThrones
import com.example.dataofthronesapi.repositories.UserPreferencesRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class UserInfoVM(
    private val userPrefsRepository: UserPreferencesRepository
) : ViewModel() {
    private val _uiState: MutableStateFlow<UserInfoUiState> = MutableStateFlow(UserInfoUiState())
    val uiState: StateFlow<UserInfoUiState> = _uiState.asStateFlow()

    init {
        viewModelScope.launch(Dispatchers.IO) {
            userPrefsRepository.getUserPrefs().collect { userPrefs ->
                _uiState.update { currentSate ->
                    currentSate.copy(
                        userName = userPrefs.name
                    )
                }
            }
        }
    }

    override fun onCleared() {
        super.onCleared()
        Log.i("GameViewModel", "GameViewModel destruido!")
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

                return UserInfoVM(
                    (application as DataOfThrones).appcontainer.userPreferencesRepository
                ) as T
            }
        }
    }
}