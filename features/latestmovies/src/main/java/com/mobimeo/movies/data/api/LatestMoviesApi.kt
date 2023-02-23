package com.mobimeo.movies.data.api

import com.mobimeo.core.model.MoviesResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.QueryMap

/**
 * Created by Rafiqul Hasan
 */
interface LatestMoviesApi {
	@GET("movie/now_playing")
	suspend fun getLatestMovies(@QueryMap queryMap: Map<String, String>): Response<MoviesResponse>
}