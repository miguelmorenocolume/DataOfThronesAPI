package com.example.dataofthronesapi.ui.characterdetails

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.CreationExtras
import com.example.dataofthronesapi.dependencies.DataOfThrones
import com.example.dataofthronesapi.repositories.CharacterRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class CharacterDetailsVM(
    private val characterRepository: CharacterRepository
) : ViewModel() {

    private val _uiState: MutableStateFlow<CharacterDetailsUiState> = MutableStateFlow(
        CharacterDetailsUiState()
    )
    val uiState: StateFlow<CharacterDetailsUiState> = _uiState.asStateFlow()


    fun setCharacter(idCharacter: Int) {
        viewModelScope.launch {
            val characterResp = characterRepository.getCharacter(idCharacter)
            if (characterResp.isSuccessful) {
                val character = characterResp.body()
                _uiState.update {
                    it.copy(
                        isLoading = false,
                        character = character
                    )
                }
            }
        }
    }

    companion object {
        val Factory: ViewModelProvider.Factory = object : ViewModelProvider.Factory {
            @Suppress("UNCHECKED_CAST")
            override fun <T : ViewModel> create(
                modelClass: Class<T>,
                extras: CreationExtras
            ): T {
                // Get the Application object from extras
                val application =
                    checkNotNull(extras[ViewModelProvider.AndroidViewModelFactory.APPLICATION_KEY])

                return CharacterDetailsVM(
                    (application as DataOfThrones).appcontainer.characterRepository
                ) as T
            }
        }
    }

}