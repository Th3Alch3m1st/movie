package com.mobimeo.searchmovies.data.api

import com.mobimeo.searchmovies.utils.TestUtils
import com.mobimeo.searchmovies.utils.TestUtils.getOkHttpClient
import com.mobimeo.searchmovies.utils.TestUtils.getQueryMap
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
class SearchMovieApiTest {
	companion object {
		const val ERROR_RESPONSE = "Invalid Token"

		const val QUERY = "Jack"
		const val PAGE_1_DATA = "page1.json"
		const val PAGE_2_DATA = "page2.json"
		const val PAGE_END_DATA = "pageEnd.json"

		const val PAGE_NO_1 = 1
		const val PAGE_NO_2 = 2
		const val PAGE_END = 3

		const val PAGE_1_0_INDEX_ID = 75780
		const val PAGE_2_0_INDEX_ID = 36950
		const val PAGE_END_0_INDEX_ID = 25143

		const val PAGE_LIMIT = 20
		const val PAGE_END_SIZE = 18
		const val TOTAL_ITEM = 58
	}

	@get:Rule
	val mockWebServer = MockWebServer()

	private lateinit var sutLatestMoviesApi: SearchMovieApi

	@Before
	fun setUp() {
		val moshi = Moshi.Builder()
			.build()

		sutLatestMoviesApi = Retrofit.Builder()
			.baseUrl(mockWebServer.url("/"))
			.addConverterFactory(MoshiConverterFactory.create(moshi))
			.client(getOkHttpClient())
			.build()
			.create(SearchMovieApi::class.java)
	}

	@After
	fun shutDown() {
		mockWebServer.shutdown()
	}

	@Test
	fun `searchMovies for first call should return first paging Data`() {
		runBlocking {
			//Arrange
			mockWebServer.enqueue(TestUtils.mockResponse(PAGE_1_DATA))

			// Act
			val response = sutLatestMoviesApi.searchMovies(getQueryMap(QUERY, PAGE_NO_1))

			// Assert
			response.body()?.totalResults shouldEqual TOTAL_ITEM
			response.body()?.results?.size shouldEqual PAGE_LIMIT
			response.body()?.results?.get(0)?.id shouldEqual PAGE_1_0_INDEX_ID
		}
	}

	@Test
	fun `searchMovies for second call should return second paging Data`() {
		runBlocking {
			//Arrange
			mockWebServer.enqueue(TestUtils.mockResponse(PAGE_2_DATA))

			// Act
			val response = sutLatestMoviesApi.searchMovies(getQueryMap(QUERY, PAGE_NO_2))

			// Assert
			response.body()?.totalResults shouldEqual TOTAL_ITEM
			response.body()?.results?.size shouldEqual PAGE_LIMIT
			response.body()?.results?.get(0)?.id shouldEqual PAGE_2_0_INDEX_ID
		}
	}

	@Test
	fun `searchMovies for end call should return end paging Data`() {
		runBlocking {
			//Arrange
			mockWebServer.enqueue(TestUtils.mockResponse(PAGE_END_DATA))

			// Act
			val response = sutLatestMoviesApi.searchMovies(getQueryMap(QUERY, PAGE_END))

			// Assert
			response.body()?.totalResults shouldEqual TOTAL_ITEM
			response.body()?.results?.size shouldEqual PAGE_END_SIZE
			response.body()?.results?.get(0)?.id shouldEqual PAGE_END_0_INDEX_ID
		}
	}
}