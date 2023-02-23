package com.mobimeo.searchmovies.di

import com.mobimeo.movies.dto.usecase.SearchMoviesUseCase
import com.mobimeo.searchmovies.dto.usecase.SearchMoviesUseCaseImpl
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
abstract class SearchMoviesUseCaseModule {

	@Binds
	@Singleton
	abstract fun provideSearchMoviesUseCase(useCaseImpl: SearchMoviesUseCaseImpl): SearchMoviesUseCase
}