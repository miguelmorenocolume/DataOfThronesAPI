package com.example.dataofthronesapi.ui.characterlist

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.CreationExtras
import com.example.dataofthronesapi.R
import com.example.dataofthronesapi.data.Character
import com.example.dataofthronesapi.data.UserPreferences
import com.example.dataofthronesapi.dependencies.DataOfThrones
import com.example.dataofthronesapi.repositories.CharacterRepository
import com.example.dataofthronesapi.repositories.FavCharacterRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch


class CharacterListVM(
    private val characterRepository: CharacterRepository,
    private val favCharacterRepository: FavCharacterRepository
) : ViewModel() {

    private val _uiState: MutableStateFlow<CharacterListUiState> =
        MutableStateFlow(CharacterListUiState())
    val uiState: StateFlow<CharacterListUiState> = _uiState.asStateFlow()


    init {
        getSomeCharactersFromRepository(NUM_CHARACTERS)
    }


    private fun getSomeCharactersFromRepository(numCharacters: Int) {
        viewModelScope.launch {
            val response = characterRepository.getSomeCharacters(numCharacters)
            if (response.isSuccessful) {
                val characters = response.body()
                _uiState.update { currentState ->
                    currentState.copy(
                        characterList = characters?.let { it.toList() } ?: emptyList<Character>(),
                        isLoading = false
                    )
                }
            } else {
                // Handle error response
            }
        }
    }


    fun insertCharacter(character: Character) {
        viewModelScope.launch {
            if (!favCharacterRepository.isCharacterExists(character.id)) {
                favCharacterRepository.insertCharacter(character)
                _uiState.update { currentState ->
                    currentState.copy(
                        message = "The character has been added to favorites"
                    )
                }

            } else {
                _uiState.update { currentState ->
                    currentState.copy(
                        message = "The character is already in favorites"
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

        const val NUM_CHARACTERS = 8

        val Factory: ViewModelProvider.Factory = object : ViewModelProvider.Factory {
            @Suppress("UNCHECKED_CAST")
            override fun <T : ViewModel> create(
                modelClass: Class<T>,
                extras: CreationExtras
            ): T {
                val application =
                    checkNotNull(extras[ViewModelProvider.AndroidViewModelFactory.APPLICATION_KEY])

                return CharacterListVM(
                    (application as DataOfThrones).appcontainer.characterRepository,
                    application.appcontainer.favCharacterRepository
                ) as T
            }
        }
    }
}