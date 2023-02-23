package com.mobimeo.searchmovies.dto.usecase

import androidx.paging.PagingData
import com.mobimeo.core.model.MovieUIModel
import com.mobimeo.movies.dto.usecase.SearchMoviesUseCase
import com.mobimeo.searchmovies.dto.repository.SearchMoviesRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

/**
 * Created by Rafiqul Hasan
 */
class SearchMoviesUseCaseImpl @Inject constructor(
	private val repository: SearchMoviesRepository
) : SearchMoviesUseCase {

	override suspend fun searchMovies(query: String): Flow<PagingData<MovieUIModel>> {
		return repository.searchMovies(query)
	}
}