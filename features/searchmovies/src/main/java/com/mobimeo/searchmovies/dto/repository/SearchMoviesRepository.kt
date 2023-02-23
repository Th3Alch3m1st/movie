package com.mobimeo.searchmovies.dto.repository

import androidx.paging.PagingData
import com.mobimeo.core.model.MovieUIModel
import kotlinx.coroutines.flow.Flow

/**
 * Created by Rafiqul Hasan
 */
interface SearchMoviesRepository {
	suspend fun searchMovies(query: String): Flow<PagingData<MovieUIModel>>
}