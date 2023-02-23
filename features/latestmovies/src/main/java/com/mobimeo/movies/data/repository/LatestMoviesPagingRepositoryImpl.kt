package com.mobimeo.movies.data.repository

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.mobimeo.core.mapper.Mapper
import com.mobimeo.core.model.MovieUIModel
import com.mobimeo.core.model.MovieInfo
import com.mobimeo.movies.data.paging.LatestMoviesPagingSource
import com.mobimeo.movies.data.remote.LatestMoviesRemoteSource
import com.mobimeo.movies.dto.repository.LatestMoviesRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

/**
 * Created by Rafiqul Hasan
 */
class LatestMoviesPagingRepositoryImpl @Inject constructor(
	private val remote: LatestMoviesRemoteSource,
	private val mapper: Mapper<MovieInfo, MovieUIModel>
) : LatestMoviesRepository {
	companion object {
		//no custom paging size movie db
		const val PAGING_SIZE = 20
	}

	override fun getLatestMovies(): Flow<PagingData<MovieUIModel>> {
		return Pager(
			config = PagingConfig(
				pageSize = PAGING_SIZE,
				prefetchDistance = PAGING_SIZE,
				initialLoadSize = PAGING_SIZE
			),
			initialKey = 1,
			pagingSourceFactory = { LatestMoviesPagingSource(remote, mapper) }
		).flow
	}
}