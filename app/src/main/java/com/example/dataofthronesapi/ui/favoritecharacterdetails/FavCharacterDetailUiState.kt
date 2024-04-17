package com.example.dataofthronesapi.ui.favoritecharacterdetails

import com.example.dataofthronesapi.data.Character
import com.example.dataofthronesapi.data.Comment

data class FavCharacterDetailUiState(
    val character: Character? = null,
    val comments: List<Comment> = emptyList(),
    val userName: String = "",
)