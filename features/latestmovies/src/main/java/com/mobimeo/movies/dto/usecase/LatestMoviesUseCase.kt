package com.mobimeo.movies.dto.usecase

import androidx.paging.PagingData
import com.mobimeo.core.model.MovieUIModel
import kotlinx.coroutines.flow.Flow

/**
 * Created by Rafiqul Hasan
 */
interface LatestMoviesUseCase {
	fun getLatestMovies(): Flow<PagingData<MovieUIModel>>
}