package com.boosterstestmovis.di

import com.boosterstestmovis.domain.MovieRepository
import com.boosterstestmovis.domain.usecase.DeleteAllMoviesUseCase
import com.boosterstestmovis.domain.usecase.DeleteFavouriteMovieUseCase
import com.boosterstestmovis.domain.usecase.DeleteMovieUseCase
import com.boosterstestmovis.domain.usecase.GetAllFavouriteMoviesUseCase
import com.boosterstestmovis.domain.usecase.GetAllMoviesUseCase
import com.boosterstestmovis.domain.usecase.GetFavouriteMovieByIdUseCase
import com.boosterstestmovis.domain.usecase.GetMovieByIdUseCase
import com.boosterstestmovis.domain.usecase.InsertFavouriteMovieUseCase
import com.boosterstestmovis.domain.usecase.InsertMovieUseCase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
object DomainModule {

    @Provides
    fun provideGetAllMoviesUseCase(repository: MovieRepository): GetAllMoviesUseCase {
        return GetAllMoviesUseCase(repository)
    }

    @Provides
    fun provideGetAllFavouriteMoviesUseCase(repository: MovieRepository): GetAllFavouriteMoviesUseCase {
        return GetAllFavouriteMoviesUseCase(repository)
    }

    @Provides
    fun provideGetMovieByIdUseCase(repository: MovieRepository): GetMovieByIdUseCase {
        return GetMovieByIdUseCase(repository)
    }

    @Provides
    fun provideGetFavouriteMovieByIdUseCase(repository: MovieRepository): GetFavouriteMovieByIdUseCase {
        return GetFavouriteMovieByIdUseCase(repository)
    }

    @Provides
    fun provideDeleteAllMoviesUseCase(repository: MovieRepository): DeleteAllMoviesUseCase {
        return DeleteAllMoviesUseCase(repository)
    }

    @Provides
    fun provideInsertMovieUseCase(repository: MovieRepository): InsertMovieUseCase {
        return InsertMovieUseCase(repository)
    }

    @Provides
    fun provideDeleteMovieUseCase(repository: MovieRepository): DeleteMovieUseCase {
        return DeleteMovieUseCase(repository)
    }

    @Provides
    fun provideInsertFavouriteMovieUseCase(repository: MovieRepository): InsertFavouriteMovieUseCase {
        return InsertFavouriteMovieUseCase(repository)
    }

    @Provides
    fun provideDeleteFavouriteMovieUseCase(repository: MovieRepository): DeleteFavouriteMovieUseCase {
        return DeleteFavouriteMovieUseCase(repository)
    }
}