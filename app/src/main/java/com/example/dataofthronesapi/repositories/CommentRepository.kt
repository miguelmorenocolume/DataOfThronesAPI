package com.example.dataofthronesapi.repositories

import com.example.dataofthronesapi.data.Comment
import com.example.dataofthronesapi.datasource.CommentDAO
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.withContext

class CommentRepository(
    private val commentDao: CommentDAO,
    private val ioDispatcher: CoroutineDispatcher = Dispatchers.IO
) {
    suspend fun insertComment(comment: Comment) =
        withContext(ioDispatcher) {
            commentDao.insertGame(comment)
        }
    
    fun getCommentbyId(commentId: Int): Flow<List<Comment>> {
        return commentDao.getCommentById(commentId)
    }
}


