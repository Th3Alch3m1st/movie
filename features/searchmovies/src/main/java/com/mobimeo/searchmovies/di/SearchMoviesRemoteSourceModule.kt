package com.mobimeo.searchmovies.di

import com.mobimeo.searchmovies.data.remote.SearchMoviesRemoteSource
import com.mobimeo.searchmovies.data.remote.SearchMoviesRemoteSourceImpl
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
abstract class SearchMoviesRemoteSourceModule {
	@Binds
	@Singleton
	abstract fun provideSearchMoviesRemoteSource(sourceImpl: SearchMoviesRemoteSourceImpl): SearchMoviesRemoteSource
}