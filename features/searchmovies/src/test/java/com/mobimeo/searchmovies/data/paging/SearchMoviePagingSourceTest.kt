package com.mobimeo.searchmovies.data.paging

import androidx.paging.PagingSource
import com.mobimeo.core.mapper.Mapper
import com.mobimeo.core.mapper.MovieInfoToMovieUIModelMapper
import com.mobimeo.core.model.MovieInfo
import com.mobimeo.core.model.MovieUIModel
import com.mobimeo.core.model.MoviesResponse
import com.mobimeo.core.network.RequestException
import com.mobimeo.core.network.Resource
import com.mobimeo.searchmovies.utils.TestUtils.getSearchMovieRemoteData
import com.mobimeo.searchmovies.data.api.SearchMovieApiTest
import com.mobimeo.searchmovies.data.remote.SearchMoviesRemoteSource
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
class SearchMoviePagingSourceTest {
	@Mock
	lateinit var mockRemoteSource: SearchMoviesRemoteSource

	private lateinit var sutPagingSource: SearchMoviesPagingSource

	private lateinit var movieResponseOne: Resource<MoviesResponse>
	private lateinit var moviesResponseTwo: Resource<MoviesResponse>
	private lateinit var moviesResponseEnd: Resource<MoviesResponse>

	private lateinit var mapper: Mapper<MovieInfo, MovieUIModel>

	@Before
	fun setup() {
		mapper = MovieInfoToMovieUIModelMapper()
		sutPagingSource = SearchMoviesPagingSource(
			SearchMovieApiTest.QUERY,
			mockRemoteSource,
			mapper
		)

		movieResponseOne = getSearchMovieRemoteData(SearchMovieApiTest.PAGE_1_DATA)
		moviesResponseTwo = getSearchMovieRemoteData(SearchMovieApiTest.PAGE_2_DATA)
		moviesResponseEnd = getSearchMovieRemoteData(SearchMovieApiTest.PAGE_END_DATA)
	}

	@Test
	fun `check argument pass correctly for first paging in searchMovies fun`() {
		runBlocking {
			//arrange
			pageOneDataSuccess()
			val acString = argumentCaptor<String>()
			val acInt = argumentCaptor<Int>()

			//act
			val refreshRequest: PagingSource.LoadParams.Refresh<Int> =
				PagingSource.LoadParams.Refresh(
					key = null,
					loadSize = SearchMovieApiTest.PAGE_LIMIT,
					placeholdersEnabled = false
				)
			sutPagingSource.load(refreshRequest)
			Mockito.verify(mockRemoteSource)
				.searchMovies(acString.capture(), acInt.capture())

			//verify
			acString.firstValue shouldEqual SearchMovieApiTest.QUERY
			acInt.firstValue shouldEqual SearchMovieApiTest.PAGE_NO_1
		}
	}

	@Test
	fun `check argument pass correctly for second paging in searchMovies fun`() {
		runBlocking {
			//arrange
			pageOneDataSuccess()
			val acString = argumentCaptor<String>()
			val acInt = argumentCaptor<Int>()

			//act
			val refreshRequest: PagingSource.LoadParams.Refresh<Int> =
				PagingSource.LoadParams.Refresh(
					key = 2,
					loadSize = SearchMovieApiTest.PAGE_LIMIT,
					placeholdersEnabled = false
				)
			sutPagingSource.load(refreshRequest)
			Mockito.verify(mockRemoteSource)
				.searchMovies(acString.capture(), acInt.capture())

			//verify
			acString.firstValue shouldEqual SearchMovieApiTest.QUERY
			acInt.firstValue shouldEqual SearchMovieApiTest.PAGE_NO_2
		}
	}

	@Test
	fun `check argument pass correctly for third paging in searchMovies fun`() {
		runBlocking {
			//arrange
			pageOneDataSuccess()
			val acString = argumentCaptor<String>()
			val acInt = argumentCaptor<Int>()

			//act
			val refreshRequest: PagingSource.LoadParams.Refresh<Int> =
				PagingSource.LoadParams.Refresh(
					key = 3,
					loadSize = SearchMovieApiTest.PAGE_LIMIT,
					placeholdersEnabled = false
				)
			sutPagingSource.load(refreshRequest)


			//verify
			Mockito.verify(mockRemoteSource)
				.searchMovies(acString.capture(), acInt.capture())
			acString.firstValue shouldEqual SearchMovieApiTest.QUERY
			acInt.firstValue shouldEqual SearchMovieApiTest.PAGE_END
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
					key = SearchMovieApiTest.PAGE_NO_1,
					loadSize = SearchMovieApiTest.PAGE_LIMIT,
					placeholdersEnabled = false
				)
			val result = sutPagingSource.load(refreshRequest)

			//verify
			result shouldEqual PagingSource.LoadResult.Page(
				data = (movieResponseOne as Resource.Success).data.results?.map(mapper::map)
					.orEmpty(),
				prevKey = null,
				nextKey = SearchMovieApiTest.PAGE_NO_2
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
					key = SearchMovieApiTest.PAGE_NO_2,
					loadSize = SearchMovieApiTest.PAGE_LIMIT,
					placeholdersEnabled = false
				)
			val result = sutPagingSource.load(refreshRequest)

			//verify
			result shouldEqual PagingSource.LoadResult.Page(
				data = (moviesResponseTwo as Resource.Success).data.results?.map(mapper::map)
					.orEmpty(),
				prevKey = SearchMovieApiTest.PAGE_NO_1,
				nextKey = SearchMovieApiTest.PAGE_END
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
					key = SearchMovieApiTest.PAGE_END,
					loadSize = SearchMovieApiTest.PAGE_LIMIT,
					placeholdersEnabled = false
				)
			val result = sutPagingSource.load(refreshRequest)

			//verify
			result shouldEqual PagingSource.LoadResult.Page(
				data = (moviesResponseEnd as Resource.Success).data.results?.map(mapper::map)
					.orEmpty(),
				prevKey = SearchMovieApiTest.PAGE_NO_2,
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
					key = SearchMovieApiTest.PAGE_NO_1,
					loadSize = SearchMovieApiTest.PAGE_LIMIT,
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
					key = SearchMovieApiTest.PAGE_NO_1,
					loadSize = SearchMovieApiTest.PAGE_LIMIT,
					placeholdersEnabled = false
				)
			val result = sutPagingSource.load(refreshRequest)

			//verify
			assertThat(result).isInstanceOf(PagingSource.LoadResult.Error::class.java)
			(result as PagingSource.LoadResult.Error).throwable.message shouldEqual SearchMovieApiTest.ERROR_RESPONSE
		}
	}

	private fun pageOneDataSuccess() {
		runBlocking {
			mockRemoteSource.searchMovies(any(), any()) returns movieResponseOne
		}
	}

	private fun pageTwoDataSuccess() {
		runBlocking {
			mockRemoteSource.searchMovies(any(), any()) returns moviesResponseTwo
		}
	}

	private fun pageEndDataSuccess() {
		runBlocking {
			mockRemoteSource.searchMovies(any(), any()) returns moviesResponseEnd
		}
	}

	private fun emptyListResponse() {
		runBlocking {
			mockRemoteSource.searchMovies(any(), any()) returns Resource.Success(
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
			mockRemoteSource.searchMovies(any(), any()) returns Resource.Error(
				RequestException(SearchMovieApiTest.ERROR_RESPONSE),
				0
			)
		}
	}
}