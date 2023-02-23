package com.mobimeo.searchmovies.utils

import com.mobimeo.core.model.MoviesResponse
import com.mobimeo.core.network.Resource
import com.squareup.moshi.JsonAdapter
import com.squareup.moshi.Moshi
import okhttp3.Dispatcher
import okhttp3.OkHttpClient
import okhttp3.mockwebserver.MockResponse
import java.io.IOException
import java.util.concurrent.AbstractExecutorService
import java.util.concurrent.ExecutorService
import java.util.concurrent.TimeUnit

/**
 * Created By Rafiqul Hasan
 */
object TestUtils {
	@Throws(IOException::class)
	private fun readFileToString(contextClass: Class<*>, fileName: String): String {
		contextClass.getResourceAsStream(fileName)
			?.bufferedReader().use {
				val jsonString = it?.readText() ?: ""
				it?.close()
				return jsonString
			}
	}

	@Throws(IOException::class)
	fun mockResponse(fileName: String): MockResponse {
		return MockResponse().setChunkedBody(
			readFileToString(TestUtils::class.java, "/$fileName"),
			32
		)
	}

	fun getSearchMovieRemoteData(fileName: String): Resource<MoviesResponse> {
		val moshi = Moshi.Builder()
			.build()
		val jsonAdapter: JsonAdapter<MoviesResponse> = moshi.adapter(MoviesResponse::class.java)
		val jsonString = readFileToString(TestUtils::class.java, "/$fileName")
		return Resource.Success(jsonAdapter.fromJson(jsonString)!!)
	}

	fun getQueryMap(query: String, pageNumber: Int): Map<String, String> {
		return mapOf(
			"api_key" to "1234-1234567",
			"query" to query,
			"page" to pageNumber.toString()
		)
	}


	fun getOkHttpClient(): OkHttpClient {
		return OkHttpClient.Builder()
			.readTimeout(30, TimeUnit.SECONDS)
			.writeTimeout(30, TimeUnit.SECONDS)
			.dispatcher(Dispatcher(immediateExecutorService()))
			.retryOnConnectionFailure(true).build()
	}

	private fun immediateExecutorService(): ExecutorService {
		return object : AbstractExecutorService() {
			override fun shutdown() {
			}

			override fun shutdownNow(): List<Runnable>? {
				return null
			}

			override fun isShutdown(): Boolean {
				return false
			}

			override fun isTerminated(): Boolean {
				return false
			}

			@Throws(InterruptedException::class)
			override fun awaitTermination(l: Long, timeUnit: TimeUnit): Boolean {
				return false
			}

			override fun execute(runnable: Runnable) {
				runnable.run()
			}
		}
	}
}