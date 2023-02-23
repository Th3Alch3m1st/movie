package com.mobimeo.movies.data.paging

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.mobimeo.core.mapper.Mapper
import com.mobimeo.core.model.MovieUIModel
import com.mobimeo.core.network.Resource
import com.mobimeo.core.model.MovieInfo
import com.mobimeo.movies.data.remote.LatestMoviesRemoteSource
import java.lang.Exception

/**
 * Created by Rafiqul Hasan
 */
class LatestMoviesPagingSource(
	private val remoteSource: LatestMoviesRemoteSource,
	private val mapper: Mapper<MovieInfo, MovieUIModel>
) : PagingSource<Int, MovieUIModel>() {

	override suspend fun load(params: LoadParams<Int>): LoadResult<Int, MovieUIModel> {
		return try {
			val pageNumber = params.key ?: 1
			val response = remoteSource.getLatestMovies(pageNumber = pageNumber)
			if (response is Resource.Success) {
				LoadResult.Page(
					data = response.data.results?.map(mapper::map).orEmpty(),
					prevKey = if(pageNumber == 1) null else pageNumber-1,
					nextKey = calculateNextPageNumberParameter(
						pageNumber = pageNumber,
						totalPage = response.data.totalPages ?: 0
					)
				)
			} else {
				LoadResult.Error((response as Resource.Error).exception)
			}
		} catch (e: Exception) {
			LoadResult.Error(e)
		}
	}

	override fun getRefreshKey(state: PagingState<Int, MovieUIModel>): Int? {
		// Try to find the page key of the closest page to anchorPosition, from
		// either the prevKey or the nextKey, but you need to handle nullability
		// here:
		//  * prevKey == null -> anchorPage is the first page.
		//  * nextKey == null -> anchorPage is the last page.
		//  * both prevKey and nextKey null -> anchorPage is the initial page, so
		//    just return null.
		return state.anchorPosition?.let { anchorPosition ->
			val anchorPage = state.closestPageToPosition(anchorPosition)
			anchorPage?.prevKey?.plus(1) ?: anchorPage?.nextKey?.minus(1)
		}
	}

	private fun calculateNextPageNumberParameter(pageNumber: Int, totalPage: Int): Int? {
		return if (pageNumber < totalPage) {
			pageNumber + 1
		} else {
			null
		}
	}
}