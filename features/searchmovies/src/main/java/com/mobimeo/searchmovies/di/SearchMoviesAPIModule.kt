package com.mobimeo.searchmovies.di


import com.mobimeo.searchmovies.data.api.SearchMovieApi
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import javax.inject.Singleton

/**
 * Created By Rafiqul Hasan
 */
@Module
@InstallIn(SingletonComponent::class)
object  SearchMoviesAPIModule {
	@Provides
	@Singleton
	fun provideMovieSearchApi(retrofit: Retrofit): SearchMovieApi {
		return retrofit.create(SearchMovieApi::class.java)
	}
}