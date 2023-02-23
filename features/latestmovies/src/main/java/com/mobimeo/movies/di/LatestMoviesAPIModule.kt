package com.mobimeo.movies.di


import com.mobimeo.movies.data.api.LatestMoviesApi
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
object  LatestMoviesAPIModule {
	@Provides
	@Singleton
	fun provideLatestMoviesApi(retrofit: Retrofit): LatestMoviesApi {
		return retrofit.create(LatestMoviesApi::class.java)
	}
}