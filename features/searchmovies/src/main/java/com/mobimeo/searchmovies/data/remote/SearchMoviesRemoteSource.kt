package com.mobimeo.searchmovies.data.remote

import com.mobimeo.core.model.MoviesResponse
import com.mobimeo.core.network.Resource

/**
 * Created by Rafiqul Hasan
 */
interface SearchMoviesRemoteSource {
	suspend fun searchMovies(query: String, pageNumber: Int): Resource<MoviesResponse>
}