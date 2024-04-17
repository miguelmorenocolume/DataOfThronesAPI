package com.example.dataofthronesapi.ui.characterdetails

import com.example.dataofthronesapi.data.Character

data class CharacterDetailsUiState(
    val isLoading: Boolean = true,
    val character: Character? = null
)
