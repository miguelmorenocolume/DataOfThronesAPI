package com.example.dataofthronesapi.dependencies

import android.content.Context
import androidx.datastore.preferences.preferencesDataStore
import com.example.dataofthronesapi.api.ApiService
import com.example.dataofthronesapi.api.GameOfThronesApiConfig
import com.example.dataofthronesapi.data.UserPreferences
import com.example.dataofthronesapi.datasource.LocalDatabase
import com.example.dataofthronesapi.repositories.CharacterRepository
import com.example.dataofthronesapi.repositories.CommentRepository
import com.example.dataofthronesapi.repositories.FavCharacterRepository
import com.example.dataofthronesapi.repositories.UserPreferencesRepository

val Context.userDataStore by preferencesDataStore(name = UserPreferences.SETTINGS_FILE)

class AppContainer(context: Context) {

    private val dataOfThronesApiService =
        GameOfThronesApiConfig.provideRetrofit().create(ApiService::class.java)

    private val _userPreferencesRepository: UserPreferencesRepository by lazy {
        UserPreferencesRepository(context.userDataStore)
    }
    val userPreferencesRepository get() = _userPreferencesRepository

    val characterRepository: CharacterRepository = CharacterRepository(dataOfThronesApiService)

    private val _favCharacterRepository: FavCharacterRepository by lazy {
        FavCharacterRepository(LocalDatabase.getDatabase(context).characterDao())
    }
    val favCharacterRepository get() = _favCharacterRepository


    private val _commentRepository: CommentRepository by lazy {
        CommentRepository(LocalDatabase.getDatabase(context).commentDao())
    }
    val commentRepository get() = _commentRepository
}