package com.mobimeo.movies.data.paging

import androidx.paging.PagingSource
import com.mobimeo.core.mapper.Mapper
import com.mobimeo.core.mapper.MovieInfoToMovieUIModelMapper
import com.mobimeo.core.model.MovieInfo
import com.mobimeo.core.model.MovieUIModel
import com.mobimeo.core.model.MoviesResponse
import com.mobimeo.core.network.RequestException
import com.mobimeo.core.network.Resource
import com.mobimeo.movies.utils.TestUtils.getLatestMoviesRemoteData
import com.mobimeo.movies.data.api.LatestMoviesApiTest
import com.mobimeo.movies.data.remote.LatestMoviesRemoteSource
import com.mobimeo.testutil.returns
import com.mobimeo.testutil.shouldEqual
import kotlinx.coroutines.runBlocking
import org.assertj.core.api.Assertions.assertThat
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.junit.MockitoJUnitRunner
import org.mockito.kotlin.any
import org.mockito.kotlin.argumentCaptor

/**
 * Created by Rafiqul Hasan
 */
@RunWith(MockitoJUnitRunner::class)
class LatestMoviesPagingSourceTest {
	@Mock
	lateinit var mockRemoteSource: LatestMoviesRemoteSource

	private lateinit var sutPagingSource: LatestMoviesPagingSource

	private lateinit var latestMoviesResponseOne: Resource<MoviesResponse>
	private lateinit var latestMoviesResponseTwo: Resource<MoviesResponse>
	private lateinit var latestMoviesResponseEnd: Resource<MoviesResponse>

	private lateinit var mapper: Mapper<MovieInfo, MovieUIModel>

	@Before
	fun setup() {
		mapper = MovieInfoToMovieUIModelMapper()
		sutPagingSource = LatestMoviesPagingSource(
			mockRemoteSource,
			mapper
		)

		latestMoviesResponseOne = getLatestMoviesRemoteData(LatestMoviesApiTest.PAGE_1_DATA)
		latestMoviesResponseTwo = getLatestMoviesRemoteData(LatestMoviesApiTest.PAGE_2_DATA)
		latestMoviesResponseEnd = getLatestMoviesRemoteData(LatestMoviesApiTest.PAGE_END_DATA)
	}

	@Test
	fun `check argument pass correctly for first paging in getLatestMovies fun`() {
		runBlocking {
			//arrange
			pageOneDataSuccess()
			val acInt = argumentCaptor<Int>()

			//act
			val refreshRequest: PagingSource.LoadParams.Refresh<Int> =
				PagingSource.LoadParams.Refresh(
					key = null,
					loadSize = LatestMoviesApiTest.PAGE_LIMIT,
					placeholdersEnabled = false
				)
			sutPagingSource.load(refreshRequest)
			Mockito.verify(mockRemoteSource)
				.getLatestMovies(acInt.capture())

			//verify
			acInt.firstValue shouldEqual LatestMoviesApiTest.PAGE_NO_1
		}
	}

	@Test
	fun `check argument pass correctly for second paging in getLatestMovies fun`() {
		runBlocking {
			//arrange
			pageTwoDataSuccess()
			val acInt = argumentCaptor<Int>()

			//act
			val refreshRequest: PagingSource.LoadParams.Refresh<Int> =
				PagingSource.LoadParams.Refresh(
					key = 2,
					loadSize = LatestMoviesApiTest.PAGE_LIMIT,
					placeholdersEnabled = false
				)
			sutPagingSource.load(refreshRequest)
			Mockito.verify(mockRemoteSource)
				.getLatestMovies(acInt.capture())

			//verify
			acInt.firstValue shouldEqual LatestMoviesApiTest.PAGE_NO_2
		}
	}

	@Test
	fun `check argument pass correctly for third paging in getLatestMovies fun`() {
		runBlocking {
			//arrange
			pageEndDataSuccess()
			val acInt = argumentCaptor<Int>()

			//act
			val refreshRequest: PagingSource.LoadParams.Refresh<Int> =
				PagingSource.LoadParams.Refresh(
					key = 3,
					loadSize = LatestMoviesApiTest.PAGE_LIMIT,
					placeholdersEnabled = false
				)
			sutPagingSource.load(refreshRequest)


			//verify
			Mockito.verify(mockRemoteSource)
				.getLatestMovies(acInt.capture())
			acInt.firstValue shouldEqual LatestMoviesApiTest.PAGE_END
		}
	}

	@Test
	fun `on Success first paging data and it should return first paging data`() {
		runBlocking {
			//arrange
			pageOneDataSuccess()

			//act
			val refreshRequest: PagingSource.LoadParams.Refresh<Int> =
				PagingSource.LoadParams.Refresh(
					key = LatestMoviesApiTest.PAGE_NO_1,
					loadSize = LatestMoviesApiTest.PAGE_LIMIT,
					placeholdersEnabled = false
				)
			val result = sutPagingSource.load(refreshRequest)

			//verify
			result shouldEqual PagingSource.LoadResult.Page(
				data = (latestMoviesResponseOne as Resource.Success).data.results?.map(mapper::map)
					.orEmpty(),
				prevKey = null,
				nextKey = LatestMoviesApiTest.PAGE_NO_2
			)
		}
	}

	@Test
	fun `on Success second paging data and it should return second paging data`() {
		runBlocking {
			//arrange
			pageTwoDataSuccess()

			//act
			val refreshRequest: PagingSource.LoadParams.Refresh<Int> =
				PagingSource.LoadParams.Refresh(
					key = LatestMoviesApiTest.PAGE_NO_2,
					loadSize = LatestMoviesApiTest.PAGE_LIMIT,
					placeholdersEnabled = false
				)
			val result = sutPagingSource.load(refreshRequest)

			//verify
			result shouldEqual PagingSource.LoadResult.Page(
				data = (latestMoviesResponseTwo as Resource.Success).data.results?.map(mapper::map)
					.orEmpty(),
				prevKey = LatestMoviesApiTest.PAGE_NO_1,
				nextKey = LatestMoviesApiTest.PAGE_END
			)
		}
	}

	@Test
	fun `on Success end paging data and it should return end paging data`() {
		runBlocking {
			//arrange
			pageEndDataSuccess()

			//act
			val refreshRequest: PagingSource.LoadParams.Refresh<Int> =
				PagingSource.LoadParams.Refresh(
					key = LatestMoviesApiTest.PAGE_END,
					loadSize = LatestMoviesApiTest.PAGE_LIMIT,
					placeholdersEnabled = false
				)
			val result = sutPagingSource.load(refreshRequest)

			//verify
			result shouldEqual PagingSource.LoadResult.Page(
				data = (latestMoviesResponseEnd as Resource.Success).data.results?.map(mapper::map)
					.orEmpty(),
				prevKey = LatestMoviesApiTest.PAGE_NO_2,
				nextKey = null
			)
		}
	}

	@Test
	fun `on empty response_paging source should return empty paging data`() {
		runBlocking {
			//arrange
			emptyListResponse()

			//act
			val refreshRequest: PagingSource.LoadParams.Refresh<Int> =
				PagingSource.LoadParams.Refresh(
					key = LatestMoviesApiTest.PAGE_NO_1,
					loadSize = LatestMoviesApiTest.PAGE_LIMIT,
					placeholdersEnabled = false
				)
			val result = sutPagingSource.load(refreshRequest)

			//verify
			result shouldEqual PagingSource.LoadResult.Page(
				data = listOf(),
				prevKey = null,
				nextKey = null
			)
		}
	}

	@Test
	fun `on error response_paging source should return error paging data`() {
		runBlocking {
			//arrange
			errorResponse()

			//act
			val refreshRequest: PagingSource.LoadParams.Refresh<Int> =
				PagingSource.LoadParams.Refresh(
					key = LatestMoviesApiTest.PAGE_NO_1,
					loadSize = LatestMoviesApiTest.PAGE_LIMIT,
					placeholdersEnabled = false
				)
			val result = sutPagingSource.load(refreshRequest)

			//verify
			assertThat(result).isInstanceOf(PagingSource.LoadResult.Error::class.java)
			(result as PagingSource.LoadResult.Error).throwable.message shouldEqual LatestMoviesApiTest.ERROR_RESPONSE
		}
	}

	private fun pageOneDataSuccess() {
		runBlocking {
			mockRemoteSource.getLatestMovies(any()) returns latestMoviesResponseOne
		}
	}

	private fun pageTwoDataSuccess() {
		runBlocking {
			mockRemoteSource.getLatestMovies(any()) returns latestMoviesResponseTwo
		}
	}

	private fun pageEndDataSuccess() {
		runBlocking {
			mockRemoteSource.getLatestMovies(any()) returns latestMoviesResponseEnd
		}
	}

	private fun emptyListResponse() {
		runBlocking {
			mockRemoteSource.getLatestMovies(any()) returns Resource.Success(
				MoviesResponse(
					page = 1,
					totalPages = 0,
					results = listOf(),
					totalResults = 0
				)
			)
		}
	}

	private fun errorResponse() {
		runBlocking {
			mockRemoteSource.getLatestMovies(any()) returns Resource.Error(
				RequestException(LatestMoviesApiTest.ERROR_RESPONSE),
				0
			)
		}
	}
}