package com.boosterstestmovis.di

import android.content.Context
import androidx.room.Room
import com.boosterstestmovis.data.MovieDao
import com.boosterstestmovis.data.MovieDatabase
import com.boosterstestmovis.data.MovieRepositoryImpl
import com.boosterstestmovis.domain.MovieRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DataModule {

    @Provides
    @Singleton
    fun provideDatabase(@ApplicationContext context: Context): MovieDatabase {
        return Room.databaseBuilder(
            context.applicationContext,
            MovieDatabase::class.java,
            "movies.db"
        ).fallbackToDestructiveMigration().build()
    }

    @Provides
    fun provideMovieDao(movieDatabase: MovieDatabase): MovieDao {
        return movieDatabase.movieDao()
    }

    @Provides
    fun provideMovieRepository(movieDao: MovieDao): MovieRepository {
        return MovieRepositoryImpl(movieDao)
    }
}
