package com.mobimeo.movies.domain

import androidx.paging.PagingData
import com.mobimeo.core.mapper.Mapper
import com.mobimeo.core.mapper.MovieInfoToMovieUIModelMapper
import com.mobimeo.core.model.MovieInfo
import com.mobimeo.core.model.MovieUIModel
import com.mobimeo.core.network.Resource
import com.mobimeo.movies.utils.TestUtils
import com.mobimeo.movies.data.api.LatestMoviesApiTest
import com.mobimeo.movies.dto.repository.LatestMoviesRepository
import com.mobimeo.movies.dto.usecase.LatestMoviesUseCaseImpl
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
import org.mockito.junit.MockitoJUnitRunner

/**
 * Created by Rafiqul Hasan
 */

@RunWith(MockitoJUnitRunner::class)
class LatestMoviesUseCaseImplTest {
	@Mock
	lateinit var mockRepository: LatestMoviesRepository

	private lateinit var sutUseCase: LatestMoviesUseCaseImpl

	private lateinit var latestMoviesResponseOne: Flow<PagingData<MovieUIModel>>
	private lateinit var emptyResponse: Flow<PagingData<MovieUIModel>>

	private lateinit var mapper: Mapper<MovieInfo, MovieUIModel>

	@Before
	fun setup() {
		sutUseCase = LatestMoviesUseCaseImpl(mockRepository)

		mapper = MovieInfoToMovieUIModelMapper()

		latestMoviesResponseOne = flowOf(
			PagingData.from(
				(TestUtils.getLatestMoviesRemoteData(LatestMoviesApiTest.PAGE_1_DATA) as Resource.Success).data.results?.map(
					mapper::map
				).orEmpty()
			)
		)
		emptyResponse = flowOf(
			PagingData.from(listOf())
		)
	}

	@Test
	fun `on Success first paging data and it should return first paging data`() {
		runBlocking {
			//arrange
			pageOneDataSuccess()

			//act
			val result =
				sutUseCase.getLatestMovies()
					.first()

			//verify
			result shouldEqual latestMoviesResponseOne.first()
		}
	}

	@Test
	fun `on empty response_paging source should return empty paging data`() {
		runBlocking {
			//arrange
			emptyListResponse()

			//act
			val result =
				sutUseCase.getLatestMovies()
					.first()

			//verify
			result shouldEqual emptyResponse.first()
		}
	}

	private fun pageOneDataSuccess() {
		runBlocking {
			mockRepository.getLatestMovies() returns latestMoviesResponseOne
		}
	}

	private fun emptyListResponse() {
		runBlocking {
			mockRepository.getLatestMovies() returns emptyResponse
		}
	}
}