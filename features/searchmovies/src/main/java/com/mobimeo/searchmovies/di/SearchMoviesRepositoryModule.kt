package com.mobimeo.searchmovies.di

import com.mobimeo.searchmovies.data.repository.SearchMoviesPagingRepositoryImpl
import com.mobimeo.searchmovies.dto.repository.SearchMoviesRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

/**
 * Created By Rafiqul Hasan
 */
@Module
@InstallIn(SingletonComponent::class)
abstract class SearchMoviesRepositoryModule {

	@Binds
	@Singleton
	abstract fun provideSearchMoviesRepository(useCaseImpl: SearchMoviesPagingRepositoryImpl): SearchMoviesRepository
}