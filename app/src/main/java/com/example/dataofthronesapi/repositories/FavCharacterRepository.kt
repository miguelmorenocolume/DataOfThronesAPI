package com.example.dataofthronesapi.repositories

import com.example.dataofthronesapi.data.Character
import com.example.dataofthronesapi.datasource.CharacterDAO
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.withContext

class FavCharacterRepository(
    private val characterDao: CharacterDAO,
    private val ioDispatcher: CoroutineDispatcher = Dispatchers.IO
) {

    suspend fun insertCharacter(character: Character) =
        withContext(ioDispatcher) {
            characterDao.insertCharacter(character)
        }

    suspend fun deleteCharacter(character: Character) = withContext(ioDispatcher) {
        characterDao.deleteCharacter(character)
    }
    
    fun getAllCharacters(): Flow<List<Character>> {
        return characterDao.getAllCharacters()
    }

    suspend fun getCharacterById(characterId: Int): Character? {
        return withContext(ioDispatcher) {
            characterDao.getCharacterById(characterId)
        }
    }

    suspend fun isCharacterExists(characterId: Int): Boolean = withContext(ioDispatcher) {
        return@withContext characterDao.getCharacterById(characterId) != null
    }
}