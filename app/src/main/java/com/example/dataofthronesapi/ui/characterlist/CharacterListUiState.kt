package com.example.dataofthronesapi.ui.characterlist

import com.example.dataofthronesapi.data.Character

data class CharacterListUiState(
    val isLoading: Boolean = true,
    val characterList: List<Character> = emptyList(),
    val message : String? = null,
)