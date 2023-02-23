package com.mobimeo.searchmovies.data.remote

import com.mobimeo.core.di.qualifiers.IoDispatcher
import com.mobimeo.core.network.BaseSource
import com.mobimeo.core.network.Resource
import com.mobimeo.core.model.MoviesResponse
import com.mobimeo.core.BuildConfig
import com.mobimeo.searchmovies.data.api.SearchMovieApi
import kotlinx.coroutines.CoroutineDispatcher
import javax.inject.Inject

/**
 * Created by Rafiqul Hasan
 */
class SearchMoviesRemoteSourceImpl @Inject constructor(
	private val api: SearchMovieApi,
	@IoDispatcher private val ioDispatcher: CoroutineDispatcher
) : SearchMoviesRemoteSource, BaseSource() {

	override suspend fun searchMovies(query: String, pageNumber: Int): Resource<MoviesResponse> {
		val queryMap = mapOf(
			"api_key" to BuildConfig.AUTH_TOKEN,
			"query" to query,
			"page" to pageNumber.toString()
		)

		return safeApiCall(ioDispatcher) {
			api.searchMovies(queryMap)
		}
	}
}