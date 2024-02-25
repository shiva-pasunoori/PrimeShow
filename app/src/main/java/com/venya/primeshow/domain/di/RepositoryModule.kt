package com.venya.primeshow.domain.di

import com.venya.primeshow.data.repository.MoviesRepositoryImpl
import com.venya.primeshow.domain.repository.MoviesRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

/**
 * Created by Shiva Pasunoori on 25,February,2024
 */
@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {
    @Binds
    @Singleton
    abstract fun bindMovieRepository(
        moviesRepositoryImpl: MoviesRepositoryImpl
    ): MoviesRepository
}