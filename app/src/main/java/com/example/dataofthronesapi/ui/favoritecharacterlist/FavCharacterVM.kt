package com.example.dataofthronesapi.ui.favoritecharacterlist

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.CreationExtras
import com.example.dataofthronesapi.data.Character
import com.example.dataofthronesapi.dependencies.DataOfThrones
import com.example.dataofthronesapi.repositories.FavCharacterRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class FavCharacterVM(

    private val favCharacterRepository: FavCharacterRepository
) : ViewModel() {
    private val _uiState: MutableStateFlow<FavCharacterUiState> =
        MutableStateFlow(FavCharacterUiState())
    val uiState: StateFlow<FavCharacterUiState> = _uiState.asStateFlow()

    init {
        viewModelScope.launch {
            favCharacterRepository.getAllCharacters().collect { characters ->
                _uiState.update {
                    it.copy(
                        characterList = characters
                    )
                }
            }
        }
    }

    fun deleteCharacter(character: Character) {
        viewModelScope.launch {
            if (favCharacterRepository.isCharacterExists(character.id)) {
                favCharacterRepository.deleteCharacter(character)
                _uiState.update { currentState ->
                    currentState.copy(
                        message = "Character deleted from favorites"
                    )
                }
            } else {
                _uiState.update { currentState ->
                    currentState.copy(
                        message = "The character is not in the database"
                    )
                }
            }
        }
    }

    fun messageShown() {
        _uiState.update {
            it.copy(
                message = null
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

                return FavCharacterVM(
                    (application as DataOfThrones).appcontainer.favCharacterRepository,
                ) as T
            }
        }
    }
}