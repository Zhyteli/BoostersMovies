package com.boosterstestmovis.data

import androidx.room.Database
import androidx.room.RoomDatabase
import com.boosterstestmovis.domain.entity.FavouriteMovie
import com.boosterstestmovis.domain.entity.Movie

@Database(entities = [Movie::class, FavouriteMovie::class], version = 1, exportSchema = false)
abstract class MovieDatabase : RoomDatabase() {
    abstract fun movieDao(): MovieDao
}

