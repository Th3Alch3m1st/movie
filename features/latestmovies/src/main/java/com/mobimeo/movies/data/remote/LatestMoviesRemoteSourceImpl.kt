package com.mobimeo.movies.data.remote

import com.mobimeo.core.di.qualifiers.IoDispatcher
import com.mobimeo.core.network.BaseSource
import com.mobimeo.core.network.Resource
import com.mobimeo.movies.data.api.LatestMoviesApi
import com.mobimeo.core.model.MoviesResponse
import com.mobimeo.core.BuildConfig
import kotlinx.coroutines.CoroutineDispatcher
import javax.inject.Inject

/**
 * Created by Rafiqul Hasan
 */
class LatestMoviesRemoteSourceImpl @Inject constructor(
	private val api: LatestMoviesApi,
	@IoDispatcher private val ioDispatcher: CoroutineDispatcher
) : LatestMoviesRemoteSource, BaseSource() {
	override suspend fun getLatestMovies(pageNumber: Int): Resource<MoviesResponse> {
		val queryMap = mapOf(
			"api_key" to BuildConfig.AUTH_TOKEN,
			"page" to pageNumber.toString()
		)
		return safeApiCall(ioDispatcher) {
			api.getLatestMovies(queryMap)
		}
	}
}