package com.example.dataofthronesapi.data


data class UserPreferences(
    val name: String = "",
    val skipIntro: Boolean = false,
    val savedSkipIntro: Boolean = false,
    val error: Boolean = false
) {

    companion object {
        const val SETTINGS_FILE = "settings"
        const val USER_NAME = "username"
        const val ANONYMOUS = "Anonymous"
        const val SKIP = "SkipIntro"
        const val SKIP_DEFAULT = false
    }
}