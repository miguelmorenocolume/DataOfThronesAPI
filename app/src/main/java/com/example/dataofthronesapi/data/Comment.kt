package com.example.dataofthronesapi.data

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity("comments")
data class Comment(
    @PrimaryKey(autoGenerate = true)
    val id: Int,
    @ColumnInfo(name = "user_name")
    val userName: String,
    @ColumnInfo(name = "text")
    val text: String,
    @ColumnInfo(name = "character_id")
    val characterId: Int

)