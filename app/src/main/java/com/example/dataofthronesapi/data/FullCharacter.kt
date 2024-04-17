package com.example.dataofthronesapi.data

import com.google.gson.annotations.SerializedName

data class FullCharacter(
    @SerializedName("id")
    val id: Int,
    @SerializedName("firstName")
    val firstName: String,
    @SerializedName("lastName")
    val lastName: String,
    @SerializedName("fullName")
    val fullName: String,
    @SerializedName("title")
    val title: String,
    @SerializedName("family")
    val family: String,
    @SerializedName("image")
    val image: String,
    @SerializedName("imageUrl")
    val imageUrl: String
) {
    fun toCharacter(): Character {
        return Character(
            id = id,
            firstName = firstName ?: "",
            lastName = lastName ?: "",
            fullName = fullName ?: "",
            title = title ?: "",
            family = family ?: "",
            image = image ?: "",
            imageUrl = imageUrl ?: ""
        )
    }
}