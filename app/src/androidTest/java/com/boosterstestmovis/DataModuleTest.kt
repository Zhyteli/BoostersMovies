package com.boosterstestmovis

import android.content.Context
import com.boosterstestmovis.data.MovieDao
import com.boosterstestmovis.data.MovieDatabase
import com.boosterstestmovis.di.DataModule
import com.boosterstestmovis.di.DomainModule
import com.boosterstestmovis.domain.MovieRepository
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import dagger.hilt.android.testing.UninstallModules
import junit.framework.TestCase.assertNotNull
import junit.framework.TestCase.assertTrue
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.mockito.Mock
import org.mockito.Mockito.mock

@HiltAndroidTest
@UninstallModules(DomainModule::class)
class DataModuleTest {

    @get:Rule
    var hiltRule = HiltAndroidRule(this)

    @Mock
    lateinit var context: Context

    @Mock
    lateinit var movieRepository: MovieRepository

    @Before
    fun init() {
        hiltRule.inject()
    }

    @Test
    fun provideDatabase_createsDatabase() {
        val database = DataModule.provideDatabase(context)
        assertNotNull(database)
        assertTrue(database is MovieDatabase)
    }

    @Test
    fun provideMovieDao_createsMovieDao() {
        val database = mock(MovieDatabase::class.java)
        val dao = DataModule.provideMovieDao(database)
        assertNotNull(dao)
        assertTrue(dao is MovieDao)
    }

    @Test
    fun provideMovieRepository_createsMovieRepository() {
        val dao = mock(MovieDao::class.java)
        val repository = DataModule.provideMovieRepository(dao)
        assertNotNull(repository)
        assertTrue(repository is MovieRepository)
    }
}
