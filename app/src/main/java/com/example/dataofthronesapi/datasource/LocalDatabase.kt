package com.example.dataofthronesapi.datasource

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.dataofthronesapi.data.Character
import com.example.dataofthronesapi.data.Comment

@Database(
    entities = [Character::class, Comment::class],
    version = 5,
    exportSchema = false
)
abstract class LocalDatabase : RoomDatabase() {
    abstract fun characterDao(): CharacterDAO
    abstract fun commentDao(): CommentDAO

    companion object {
        @Volatile
        private var Instance: LocalDatabase? = null

        fun getDatabase(context: Context): LocalDatabase {
            return Instance ?: synchronized(this) {
                Room.databaseBuilder(
                    context,
                    LocalDatabase::class.java,
                    "data_of_thrones_api_database"
                )
                    // Setting this option in your app's database builder means that Room
                    // permanently deletes all data from the tables in your database when it
                    // attempts to perform a migration with no defined migration path.
                    .fallbackToDestructiveMigration()
                    .build()
                    .also { Instance = it }
            }
        }
    }
}











