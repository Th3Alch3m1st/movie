package com.mobimeo.movies.data.api

import com.mobimeo.movies.utils.TestUtils
import com.mobimeo.movies.utils.TestUtils.getOkHttpClient
import com.mobimeo.movies.utils.TestUtils.getQueryMap
import com.mobimeo.testutil.shouldEqual
import com.squareup.moshi.Moshi
import kotlinx.coroutines.runBlocking
import okhttp3.mockwebserver.MockWebServer
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory

/**
 * Created by Rafiqul Hasan
 */
@RunWith(JUnit4::class)
class LatestMoviesApiTest {
	companion object {
		const val ERROR_RESPONSE = "Invalid Token"

		const val PAGE_1_DATA = "page1.json"
		const val PAGE_2_DATA = "page2.json"
		const val PAGE_END_DATA = "pageEnd.json"

		const val PAGE_NO_1 = 1
		const val PAGE_NO_2 = 2
		const val PAGE_END = 3

		const val PAGE_1_0_INDEX_ID = 315162
		const val PAGE_2_0_INDEX_ID = 866413
		const val PAGE_END_0_INDEX_ID = 760204

		const val PAGE_LIMIT = 20
		const val PAGE_END_SIZE = 19
		const val TOTAL_ITEM = 59
	}

	@get:Rule
	val mockWebServer = MockWebServer()

	private lateinit var sutLatestMoviesApi: LatestMoviesApi

	@Before
	fun setUp() {
		val moshi = Moshi.Builder()
			.build()

		sutLatestMoviesApi = Retrofit.Builder()
			.baseUrl(mockWebServer.url("/"))
			.addConverterFactory(MoshiConverterFactory.create(moshi))
			.client(getOkHttpClient())
			.build()
			.create(LatestMoviesApi::class.java)
	}

	@After
	fun shutDown() {
		mockWebServer.shutdown()
	}

	@Test
	fun `getLatestMovies for first call should return first paging Data`() {
		runBlocking {
			//Arrange
			mockWebServer.enqueue(TestUtils.mockResponse(PAGE_1_DATA))

			// Act
			val response = sutLatestMoviesApi.getLatestMovies(getQueryMap(PAGE_NO_1))

			// Assert
			response.body()?.totalResults shouldEqual TOTAL_ITEM
			response.body()?.results?.size shouldEqual PAGE_LIMIT
			response.body()?.results?.get(0)?.id shouldEqual PAGE_1_0_INDEX_ID
		}
	}

	@Test
	fun `getLatestMovies for second call should return second paging Data`() {
		runBlocking {
			//Arrange
			mockWebServer.enqueue(TestUtils.mockResponse(PAGE_2_DATA))

			// Act
			val response = sutLatestMoviesApi.getLatestMovies(getQueryMap(PAGE_NO_2))

			// Assert
			response.body()?.totalResults shouldEqual TOTAL_ITEM
			response.body()?.results?.size shouldEqual PAGE_LIMIT
			response.body()?.results?.get(0)?.id shouldEqual PAGE_2_0_INDEX_ID
		}
	}

	@Test
	fun `getLatestMovies for end call should return end paging Data`() {
		runBlocking {
			//Arrange
			mockWebServer.enqueue(TestUtils.mockResponse(PAGE_END_DATA))

			// Act
			val response = sutLatestMoviesApi.getLatestMovies(getQueryMap(PAGE_END))

			// Assert
			response.body()?.totalResults shouldEqual TOTAL_ITEM
			response.body()?.results?.size shouldEqual PAGE_END_SIZE
			response.body()?.results?.get(0)?.id shouldEqual PAGE_END_0_INDEX_ID
		}
	}
}