package com.mobimeo.searchmovies

import com.mobimeo.searchmovies.data.api.SearchMovieApiTest
import com.mobimeo.searchmovies.data.paging.SearchMoviePagingSourceTest
import com.mobimeo.searchmovies.data.remote.SearchMovieRemoteSourceImplTest
import com.mobimeo.searchmovies.domain.SearchMovieUseCaseImplTest
import com.mobimeo.searchmovies.presentation.SearchMoviesViewModelTest
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.junit.runner.RunWith
import org.junit.runners.Suite
import org.junit.runners.Suite.SuiteClasses

/**
 * Created by Rafiqul Hasan
 */
@ExperimentalCoroutinesApi
@RunWith(Suite::class)
@SuiteClasses(
	SearchMovieApiTest::class,
	SearchMovieRemoteSourceImplTest::class,
	SearchMoviePagingSourceTest::class,
	SearchMovieUseCaseImplTest::class,
	SearchMoviesViewModelTest::class
)
class SearchMoviesTestSuite