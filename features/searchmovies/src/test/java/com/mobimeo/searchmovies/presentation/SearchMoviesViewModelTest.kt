package com.mobimeo.searchmovies.presentation

import androidx.paging.PagingData
import com.mobimeo.core.mapper.Mapper
import com.mobimeo.core.mapper.MovieInfoToMovieUIModelMapper
import com.mobimeo.core.model.MovieInfo
import com.mobimeo.core.model.MovieUIModel
import com.mobimeo.core.network.Resource
import com.mobimeo.movies.dto.usecase.SearchMoviesUseCase
import com.mobimeo.searchmovies.utils.TestUtils
import com.mobimeo.searchmovies.data.api.SearchMovieApiTest
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
import org.mockito.Mockito
import org.mockito.junit.MockitoJUnitRunner
import org.mockito.kotlin.any
import org.mockito.kotlin.argumentCaptor

/**
 * Created by Rafiqul Hasan
 */
@ExperimentalCoroutinesApi
@RunWith(MockitoJUnitRunner::class)
class SearchMoviesViewModelTest {
	@get:Rule
	val dispatcherRule = MainDispatcherRule()

	@Mock
	lateinit var mockUseCase: SearchMoviesUseCase

	private lateinit var sutViewModel: SearchMoviesViewModel
	private lateinit var mapper: Mapper<MovieInfo, MovieUIModel>
	private lateinit var movieSearchResponseOne: Flow<PagingData<MovieUIModel>>
	private lateinit var movieSearchResponseTwo: Flow<PagingData<MovieUIModel>>
	private lateinit var emptyResponse: Flow<PagingData<MovieUIModel>>

	@Before
	fun setUp() {
		sutViewModel = SearchMoviesViewModel(useCase = mockUseCase)

		mapper = MovieInfoToMovieUIModelMapper()

		movieSearchResponseOne = flowOf(
			PagingData.from(
				(TestUtils.getSearchMovieRemoteData(SearchMovieApiTest.PAGE_1_DATA) as Resource.Success).data.results?.map(
					mapper::map
				).orEmpty()
			)
		)

		movieSearchResponseTwo = flowOf(
			PagingData.from(
				(TestUtils.getSearchMovieRemoteData(SearchMovieApiTest.PAGE_2_DATA) as Resource.Success).data.results?.map(
					mapper::map
				).orEmpty()
			)
		)
		emptyResponse = flowOf(
			PagingData.from(listOf())
		)
	}

	@Test
	fun `check argument pass correctly for first paging in searchMovies fun`() = runTest {
		//Arrange
		pageOneDataSuccess()
		val acString = argumentCaptor<String>()

		//act
		//this should trigger
		sutViewModel.searchMoviesTest.first()

		//verify
		Mockito.verify(mockUseCase).searchMovies(acString.capture())

		acString.firstValue shouldEqual SearchMoviesViewModel.DEFAULT_QUERY
	}

	@Test
	fun `searchMovies should return correct paging Data`() = runTest {
		//Arrange
		pageOneDataSuccess()

		//Act
		sutViewModel.searchMovies(SearchMovieApiTest.QUERY)


		//Verify
		val result = sutViewModel.searchMoviesTest.first()
		result shouldEqual movieSearchResponseOne.first()
	}

	@Test
	fun `searchMovies called two times and it should return correct paging Dat and use case was called two times`() =
		runTest {
			//search first time
			//Arrange
			pageOneDataSuccess()
			val acString = argumentCaptor<String>()

			//Act
			sutViewModel.searchMovies(SearchMovieApiTest.QUERY)
			sutViewModel.searchMoviesTest.first()

			//wait
			Thread.sleep(100)

			//search second time
			//Arrange
			pageTwoDataSuccess()
			sutViewModel.searchMovies(SearchMovieApiTest.QUERY)

			//Act
			val result = sutViewModel.searchMoviesTest.first()

			//Verify
			Mockito.verify(mockUseCase, Mockito.times(2)).searchMovies(acString.capture())
			result shouldEqual movieSearchResponseTwo.first()
		}

	@Test
	fun `searchMovies with random query for empty result and should return empty paging Data`() =
		runTest {
			//Arrange
			emptyListResponse()

			//Act
			sutViewModel.searchMovies(SearchMovieApiTest.QUERY)


			//Verify
			val result = sutViewModel.searchMoviesTest.first()
			result shouldEqual emptyResponse.first()
		}

	private fun pageOneDataSuccess() {
		runBlocking {
			mockUseCase.searchMovies(any()) returns movieSearchResponseOne
		}
	}

	private fun pageTwoDataSuccess() {
		runBlocking {
			mockUseCase.searchMovies(any()) returns movieSearchResponseTwo
		}
	}

	private fun emptyListResponse() {
		runBlocking {
			mockUseCase.searchMovies(any()) returns emptyResponse
		}
	}
}