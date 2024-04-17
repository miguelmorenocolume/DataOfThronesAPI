package com.example.dataofthronesapi.repositories

import com.example.dataofthronesapi.api.ApiService
import com.example.dataofthronesapi.data.Character
import retrofit2.Response
import kotlin.random.Random

class CharacterRepository(private val dataOfThronesApiService: ApiService) {

    companion object {
        const val NUM_CHARACTERS = 53
    }

    private val usedIds = mutableSetOf<Int>()

    suspend fun getCharacter(id: Int): Response<Character> {
        val fullCharacterResp = dataOfThronesApiService.getFullCharacter(id)
        return if (fullCharacterResp.isSuccessful) {
            val fullCharacter = fullCharacterResp.body()
            if (fullCharacter != null) {
                val character = fullCharacter.toCharacter()
                Response.success(character)
            } else {
                Response.error(404, null)
            }
        } else {
            Response.error(fullCharacterResp.code(), fullCharacterResp.errorBody())
        }
    }

    suspend fun getRandCharacter(): Response<Character> {
        var characterResponse: Response<Character>
        var character: Character?
        var id: Int
        do {
            val seed = System.currentTimeMillis()
            id = (1..NUM_CHARACTERS).random(Random(seed))
            characterResponse = getCharacter(id)
            character = characterResponse.body()
        } while (character == null || id in usedIds)
        usedIds.add(id)
        return characterResponse
    }

    suspend fun getSomeCharacters(num: Int): Response<List<Character>> {
        val characterList: MutableList<Character> = mutableListOf()
        for (i in 1..num) {
            val characterResp = getRandCharacter()
            if (characterResp.isSuccessful) {
                characterResp.body()?.let { characterList.add(it) }
            } else {
                return Response.error(null, null)
            }
        }
        return Response.success(characterList)
    }
}
