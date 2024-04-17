package com.example.dataofthronesapi.ui.favoritecharacterdetails

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.CreationExtras
import com.example.dataofthronesapi.data.Comment
import com.example.dataofthronesapi.dependencies.DataOfThrones
import com.example.dataofthronesapi.repositories.CommentRepository
import com.example.dataofthronesapi.repositories.FavCharacterRepository
import com.example.dataofthronesapi.repositories.UserPreferencesRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class FavCharacterDetailsVM(
    private val favCharacterRepository: FavCharacterRepository,
    private val commentRepository: CommentRepository,
    private val userPreferencesRepository: UserPreferencesRepository
) : ViewModel() {

    private val _uiState: MutableStateFlow<FavCharacterDetailUiState> = MutableStateFlow(
        FavCharacterDetailUiState()
    )
    val uiState: StateFlow<FavCharacterDetailUiState> = _uiState.asStateFlow()


    fun setCharacter(idCharacter: Int) {
        viewModelScope.launch {
            val character = favCharacterRepository.getCharacterById(idCharacter)
            commentRepository.getCommentbyId(idCharacter).collect { comment ->
                _uiState.update {
                    it.copy(
                        character = character,
                        comments = comment
                    )
                }
            }
        }
    }

    fun setComment(text: String, idCharacter: Int) {
        viewModelScope.launch {
            userPreferencesRepository.getUserPrefs().collect { userPrefs ->
                val userName = userPrefs.name
                val comment = Comment(
                    id = 0,
                    userName = userName,
                    text = text,
                    characterId = idCharacter
                )
                commentRepository.insertComment(comment)
                val updatedComments = _uiState.value.comments.toMutableList().apply {
                    add(comment)
                }
                _uiState.update {
                    it.copy(
                        comments = updatedComments
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

                return FavCharacterDetailsVM(
                    (application as DataOfThrones).appcontainer.favCharacterRepository,
                    application.appcontainer.commentRepository,
                    application.appcontainer.userPreferencesRepository
                ) as T
            }
        }
    }
}