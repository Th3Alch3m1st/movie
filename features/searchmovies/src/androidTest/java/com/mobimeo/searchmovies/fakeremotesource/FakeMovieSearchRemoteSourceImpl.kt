package com.mobimeo.searchmovies.fakeremotesource

import com.mobimeo.core.model.MovieInfo
import com.mobimeo.core.model.MoviesResponse
import com.mobimeo.core.network.RequestException
import com.mobimeo.core.network.Resource
import com.mobimeo.searchmovies.data.remote.SearchMoviesRemoteSource
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Created by Rafiqul Hasan
 */
@Singleton
class FakeMovieSearchRemoteSourceImpl @Inject constructor() : SearchMoviesRemoteSource {
	companion object {
		const val TRIGGERED_EMPTY_RESPONSE = "jack asdadasd"
		const val MSG_ERROR = "Invalid Token"
		const val MSG_EMPTY = "Nothing found, try with a different title"
		const val PAGE_SIZE = 20

	}

	var isTestError = false
	var isRetryTest = false


	override suspend fun searchMovies(query: String, pageNumber: Int): Resource<MoviesResponse> {

		if (isTestError) {
			return Resource.Error(RequestException(MSG_ERROR), 0)
		} else if (query.equals(TRIGGERED_EMPTY_RESPONSE, true)) {
			return Resource.Success(MoviesResponse())
		} else {
			val response = if (isRetryTest) {
				MoviesResponse(
					totalPages = 10,
					page = pageNumber,
					results = getMovies()
				)
			} else {
				MoviesResponse(
					totalPages = 1,
					page = pageNumber,
					results = getMovies()
				)
			}
			return Resource.Success(response)
		}
	}


	private fun getMovies(pageLimit: Int = PAGE_SIZE): List<MovieInfo> {
		val list = mutableListOf<MovieInfo>()
		for (i in 0 until pageLimit) {
			val image = if (i % 5 == 0) {
				getMovie(
					"/faXT8V80JRhnArTAeYXz0Eutpv9.jpg",
					"Puss in Boots: The Last Wish"
				)
			} else if (i % 5 == 1) {
				getMovie(
					"/zGoZB4CboMzY1z4G3nU6BWnMDB2.jpg",
					"Shotgun Wedding"
				)
			} else if (i % 5 == 2) {
				getMovie(
					"/q2fY4kMXKoGv4CQf310MCxpXlRI.jpg",
					"M3GAN"
				)
			} else if (i % 5 == 3) {
				getMovie(
					"/s16H6tpK2utvwDtzZ8Qy4qm5Emw.jpg",
					"Avatar: The Way of Water"
				)
			} else {
				getMovie(
					"/26yQPXymbWeCLKwcmyL8dRjAzth.jpg",
					"Devotion"
				)
			}
			list.add(image)
		}
		return list
	}

	private var id = 0
	private fun getMovie(image: String, movieTitle: String): MovieInfo {
		id++
		return MovieInfo(
			id = id,
			backdropPath = image,
			title = movieTitle,
			overview = "Set more than a decade after the events of the first film, learn the story of the Sully family (Jake, Neytiri, and their kids), the trouble that follows them, the lengths they go to keep each other safe, the battles they fight to stay alive, and the tragedies they endure."
		)
	}
}