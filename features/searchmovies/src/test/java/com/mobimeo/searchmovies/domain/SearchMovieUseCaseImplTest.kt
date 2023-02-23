package com.mobimeo.searchmovies.domain

import androidx.paging.PagingData
import com.mobimeo.core.mapper.Mapper
import com.mobimeo.core.mapper.MovieInfoToMovieUIModelMapper
import com.mobimeo.core.model.MovieInfo
import com.mobimeo.core.model.MovieUIModel
import com.mobimeo.core.network.Resource
import com.mobimeo.searchmovies.utils.TestUtils
import com.mobimeo.searchmovies.data.api.SearchMovieApiTest
import com.mobimeo.searchmovies.dto.repository.SearchMoviesRepository
import com.mobimeo.searchmovies.dto.usecase.SearchMoviesUseCaseImpl
import com.mobimeo.testutil.returns
import com.mobimeo.testutil.shouldEqual
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.runBlocking
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
class SearchMovieUseCaseImplTest {
	@Mock
	lateinit var mockRepository: SearchMoviesRepository

	private lateinit var sutUseCase: SearchMoviesUseCaseImpl

	private lateinit var movieSearchResponseOne: Flow<PagingData<MovieUIModel>>
	private lateinit var emptyResponse: Flow<PagingData<MovieUIModel>>

	private lateinit var mapper: Mapper<MovieInfo, MovieUIModel>

	@Before
	fun setup() {
		sutUseCase = SearchMoviesUseCaseImpl(mockRepository)

		mapper = MovieInfoToMovieUIModelMapper()

		movieSearchResponseOne = flowOf(
			PagingData.from(
				(TestUtils.getSearchMovieRemoteData(SearchMovieApiTest.PAGE_1_DATA) as Resource.Success).data.results?.map(
					mapper::map
				).orEmpty()
			)
		)
		emptyResponse = flowOf(
			PagingData.from(listOf())
		)
	}

	@Test
	fun `check argument pass correctly for first paging in searchMovies fun`() {
		runBlocking {
			//arrange
			pageOneDataSuccess()
			val acString = argumentCaptor<String>()

			//act
			sutUseCase.searchMovies(SearchMovieApiTest.QUERY)

			//verify
			Mockito.verify(mockRepository)
				.searchMovies(acString.capture())
			acString.firstValue shouldEqual SearchMovieApiTest.QUERY
		}
	}

	@Test
	fun `on Success first paging data and it should return first paging data`() {
		runBlocking {
			//arrange
			pageOneDataSuccess()

			//act
			val result =
				sutUseCase.searchMovies(SearchMovieApiTest.QUERY)
					.first()

			//verify
			result shouldEqual movieSearchResponseOne.first()
		}
	}

	@Test
	fun `on empty response_paging source should return empty paging data`() {
		runBlocking {
			//arrange
			emptyListResponse()

			//act
			val result =
				sutUseCase.searchMovies(SearchMovieApiTest.QUERY)
					.first()

			//verify
			result shouldEqual emptyResponse.first()
		}
	}

	private fun pageOneDataSuccess() {
		runBlocking {
			mockRepository.searchMovies(any()) returns movieSearchResponseOne
		}
	}

	private fun emptyListResponse() {
		runBlocking {
			mockRepository.searchMovies(any()) returns emptyResponse
		}
	}
}