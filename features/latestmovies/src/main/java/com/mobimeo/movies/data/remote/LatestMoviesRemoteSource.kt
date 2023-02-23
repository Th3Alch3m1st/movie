package com.mobimeo.movies.data.remote

import com.mobimeo.core.network.Resource
import com.mobimeo.core.model.MoviesResponse

/**
 * Created by Rafiqul Hasan
 */
interface LatestMoviesRemoteSource {
	suspend fun getLatestMovies(pageNumber:Int): Resource<MoviesResponse>
}