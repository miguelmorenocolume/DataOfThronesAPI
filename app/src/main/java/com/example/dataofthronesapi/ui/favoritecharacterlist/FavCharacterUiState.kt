package com.example.dataofthronesapi.ui.favoritecharacterlist

import com.example.dataofthronesapi.data.Character

data class FavCharacterUiState(
    val characterList: List<Character> = emptyList(),
    val message: String? = null
)
