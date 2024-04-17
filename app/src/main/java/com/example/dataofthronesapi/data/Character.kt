package com.example.dataofthronesapi.data

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity("characters")
data class Character(
    @PrimaryKey(autoGenerate = true)
    val id: Int,
    @ColumnInfo(name = "first_name")
    val firstName: String,
    @ColumnInfo(name = "last_name")
    val lastName: String,
    @ColumnInfo(name = "full_name")
    val fullName: String,
    @ColumnInfo(name = "title")
    val title: String,
    @ColumnInfo(name = "family")
    val family: String,
    @ColumnInfo(name = "image")
    val image: String,
    @ColumnInfo(name = "image_url")
    val imageUrl: String
)
