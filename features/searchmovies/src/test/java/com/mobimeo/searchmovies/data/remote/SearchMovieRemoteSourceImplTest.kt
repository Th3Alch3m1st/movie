package com.mobimeo.searchmovies.data.remote

import com.mobimeo.core.network.Resource
import com.mobimeo.searchmovies.utils.TestUtils
import com.mobimeo.searchmovies.utils.TestUtils.getOkHttpClient
import com.mobimeo.searchmovies.data.api.SearchMovieApi
import com.mobimeo.searchmovies.data.api.SearchMovieApiTest
import com.mobimeo.testutil.shouldEqual
import com.squareup.moshi.Moshi
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.test.UnconfinedTestDispatcher
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
@OptIn(ExperimentalCoroutinesApi::class)
@RunWith(JUnit4::class)
class SearchMovieRemoteSourceImplTest {
	@get:Rule
	val mockWebServer = MockWebServer()

	private lateinit var api: SearchMovieApi
	private lateinit var sutSearchMovieRemoteSourceImpl: SearchMoviesRemoteSourceImpl

	@Before
	fun setUp() {
		val moshi = Moshi.Builder()
			.build()

		api = Retrofit.Builder()
			.baseUrl(mockWebServer.url("/"))
			.addConverterFactory(MoshiConverterFactory.create(moshi))
			.client(getOkHttpClient())
			.build()
			.create(SearchMovieApi::class.java)

		sutSearchMovieRemoteSourceImpl =
			SearchMoviesRemoteSourceImpl(api, UnconfinedTestDispatcher())
	}

	@After
	fun shutDown() {
		mockWebServer.shutdown()
	}

	@Test
	fun `searchMovies for first call should return first paging Data`() {
		runBlocking {
			//Arrange
			mockWebServer.enqueue(TestUtils.mockResponse(SearchMovieApiTest.PAGE_1_DATA))

			// Act
			val response = sutSearchMovieRemoteSourceImpl.searchMovies(
				SearchMovieApiTest.QUERY,
				SearchMovieApiTest.PAGE_NO_1
			)

			// Assert
			(response as Resource.Success).data.totalResults shouldEqual SearchMovieApiTest.TOTAL_ITEM
			response.data.results?.size shouldEqual SearchMovieApiTest.PAGE_LIMIT
			response.data.results?.get(0)?.id shouldEqual SearchMovieApiTest.PAGE_1_0_INDEX_ID
		}
	}

	@Test
	fun `searchMovies for second call should return second paging Data`() {
		runBlocking {
			//Arrange
			mockWebServer.enqueue(TestUtils.mockResponse(SearchMovieApiTest.PAGE_2_DATA))

			// Act
			val response = sutSearchMovieRemoteSourceImpl.searchMovies(
				SearchMovieApiTest.QUERY,
				SearchMovieApiTest.PAGE_NO_2
			)

			// Assert
			(response as Resource.Success).data.totalResults shouldEqual SearchMovieApiTest.TOTAL_ITEM
			response.data.results?.size shouldEqual SearchMovieApiTest.PAGE_LIMIT
			response.data.results?.get(0)?.id shouldEqual SearchMovieApiTest.PAGE_2_0_INDEX_ID
		}
	}

	@Test
	fun `searchMovies for end call should return end paging Data`() {
		runBlocking {
			//Arrange
			mockWebServer.enqueue(TestUtils.mockResponse(SearchMovieApiTest.PAGE_END_DATA))

			// Act
			val response = sutSearchMovieRemoteSourceImpl.searchMovies(
				SearchMovieApiTest.QUERY,
				SearchMovieApiTest.PAGE_END
			)

			// Assert
			(response as Resource.Success).data.totalResults shouldEqual SearchMovieApiTest.TOTAL_ITEM
			response.data.results?.size shouldEqual SearchMovieApiTest.PAGE_END_SIZE
			response.data.results?.get(0)?.id shouldEqual SearchMovieApiTest.PAGE_END_0_INDEX_ID
		}
	}
}