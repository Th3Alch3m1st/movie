package com.mobimeo.movies.dto.usecase

import androidx.paging.PagingData
import com.mobimeo.core.model.MovieUIModel
import com.mobimeo.movies.dto.repository.LatestMoviesRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

/**
 * Created by Rafiqul Hasan
 */
class LatestMoviesUseCaseImpl @Inject constructor(
	private val repository: LatestMoviesRepository
) : LatestMoviesUseCase {

	override fun getLatestMovies(): Flow<PagingData<MovieUIModel>> {
		return repository.getLatestMovies()
	}
}