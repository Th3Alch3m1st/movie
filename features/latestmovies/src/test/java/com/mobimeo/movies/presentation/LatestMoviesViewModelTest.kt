package com.mobimeo.movies.presentation

import androidx.paging.PagingData
import com.mobimeo.core.mapper.Mapper
import com.mobimeo.core.mapper.MovieInfoToMovieUIModelMapper
import com.mobimeo.core.model.MovieInfo
import com.mobimeo.core.model.MovieUIModel
import com.mobimeo.core.network.Resource
import com.mobimeo.movies.utils.TestUtils
import com.mobimeo.movies.data.api.LatestMoviesApiTest
import com.mobimeo.movies.dto.usecase.LatestMoviesUseCase
import com.mobimeo.testutil.MainDispatcherRule
import com.mobimeo.testutil.returns
import com.mobimeo.testutil.shouldEqual
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.junit.MockitoJUnitRunner

/**
 * Created by Rafiqul Hasan
 */
@ExperimentalCoroutinesApi
@RunWith(MockitoJUnitRunner::class)
class LatestMoviesViewModelTest {
	@get:Rule
	val dispatcherRule = MainDispatcherRule()

	@Mock
	lateinit var mockUseCase: LatestMoviesUseCase

	private lateinit var sutViewModel: LatestMoviesViewModel
	private lateinit var mapper: Mapper<MovieInfo, MovieUIModel>
	private lateinit var latestMoviesResponseOne: Flow<PagingData<MovieUIModel>>
	private lateinit var emptyResponse: Flow<PagingData<MovieUIModel>>

	@Before
	fun setUp() {
		sutViewModel = LatestMoviesViewModel(useCase = mockUseCase)

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
	fun `getLatestMovies should return correct paging Data`() = runTest {
		//Arrange
		pageOneDataSuccess()

		//act
		//this should trigger
		val result = sutViewModel.latestMoviesTest.first()


		//Verify
		result shouldEqual latestMoviesResponseOne.first()
	}

	@Test
	fun `getLatestMovies for empty result and should return empty paging Data`() =
		runTest {
			//Arrange
			emptyListResponse()

			//Act
			val result = sutViewModel.latestMoviesTest.first()


			//Verify
			result shouldEqual emptyResponse.first()
		}

	private fun pageOneDataSuccess() {
		runBlocking {
			mockUseCase.getLatestMovies() returns latestMoviesResponseOne
		}
	}

	private fun emptyListResponse() {
		runBlocking {
			mockUseCase.getLatestMovies() returns emptyResponse
		}
	}
}