package com.mobimeo.core.di

import com.mobimeo.core.mapper.Mapper
import com.mobimeo.core.mapper.MovieInfoToMovieUIModelMapper
import com.mobimeo.core.model.MovieInfo
import com.mobimeo.core.model.MovieUIModel
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

/**
 * Created by Rafiqul Hasan
 */
@InstallIn(SingletonComponent::class)
@Module
object MapperModule {
	@Provides
	@Singleton
	fun provideMapper(): Mapper<MovieInfo, MovieUIModel> {
		return MovieInfoToMovieUIModelMapper()
	}
}