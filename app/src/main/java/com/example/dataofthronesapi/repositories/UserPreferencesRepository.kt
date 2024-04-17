package com.example.dataofthronesapi.repositories

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey
import com.example.dataofthronesapi.data.UserPreferences
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class UserPreferencesRepository(
    private val userDataStore: DataStore<Preferences>
) {

    fun getUserPrefs(): Flow<UserPreferences> {
        return userDataStore.data.map { userPreferences ->
            val name = userPreferences[stringPreferencesKey(UserPreferences.USER_NAME)]
                ?: UserPreferences.ANONYMOUS

            val checked = userPreferences[booleanPreferencesKey(UserPreferences.SKIP)]
                ?: UserPreferences.SKIP_DEFAULT

            return@map UserPreferences(
                name = name,
                skipIntro = checked
            )
        }
    }
    
    suspend fun saveName(name: String) {
        userDataStore.edit { userPreferences ->
            userPreferences[stringPreferencesKey(UserPreferences.USER_NAME)] = name
        }
    }

    suspend fun saveSettings(name: String, checked: Boolean) {
        userDataStore.edit { userPreferences ->
            userPreferences[stringPreferencesKey(UserPreferences.USER_NAME)] = name
            userPreferences[booleanPreferencesKey(UserPreferences.SKIP)] = checked

        }
    }

    suspend fun saveSettingsStart(checked: Boolean) {
        userDataStore.edit { userPreferences ->
            userPreferences[booleanPreferencesKey(UserPreferences.SKIP)] = checked

        }
    }
}