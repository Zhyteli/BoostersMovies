package com.boosterstestmovis.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.boosterstestmovis.domain.FavouriteMovie
import com.boosterstestmovis.domain.Movie

@Database(entities = [Movie::class, FavouriteMovie::class], version = 1, exportSchema = false)
abstract class MovieDatabase : RoomDatabase() {

    abstract fun movieDao(): MovieDao

    companion object {
        private const val DB_NAME = "movies.db"
        @Volatile
        private var database: MovieDatabase? = null
        private val LOCK = Any()

        fun getInstance(context: Context): MovieDatabase {
            return database ?: synchronized(LOCK) {
                database ?: Room.databaseBuilder(
                    context.applicationContext,
                    MovieDatabase::class.java,
                    DB_NAME
                )
                    .fallbackToDestructiveMigration()
                    .build()
                    .also { database = it }
            }
        }
    }
}
