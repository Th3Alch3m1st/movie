package com.mobimeo.movies.dto.repository

import androidx.paging.PagingData
import com.mobimeo.core.model.MovieUIModel
import kotlinx.coroutines.flow.Flow

/**
 * Created by Rafiqul Hasan
 */
interface LatestMoviesRepository {
	fun getLatestMovies(): Flow<PagingData<MovieUIModel>>
}