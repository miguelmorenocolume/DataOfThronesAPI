package com.example.dataofthronesapi.datasource

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.example.dataofthronesapi.data.Character
import com.example.dataofthronesapi.data.Comment
import kotlinx.coroutines.flow.Flow

@Dao
interface CommentDAO {
    @Insert
    suspend fun insertGame(comment: Comment)

    @Query("SELECT * FROM comments ORDER BY id DESC")
    fun getAllComments(): Flow<List<Comment>>

    @Query("SELECT * FROM comments WHERE character_id = :characterId ORDER BY id DESC")
    fun getCommentById(characterId: Int): Flow<List<Comment>>
}