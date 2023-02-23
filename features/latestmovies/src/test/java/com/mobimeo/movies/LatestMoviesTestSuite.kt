package com.mobimeo.movies

import com.mobimeo.movies.data.api.LatestMoviesApiTest
import com.mobimeo.movies.data.paging.LatestMoviesPagingSourceTest
import com.mobimeo.movies.data.remote.LatestMovieRemoteSourceImplTest
import com.mobimeo.movies.domain.LatestMoviesUseCaseImplTest
import com.mobimeo.movies.presentation.LatestMoviesViewModelTest
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
	LatestMoviesApiTest::class,
	LatestMovieRemoteSourceImplTest::class,
	LatestMoviesPagingSourceTest::class,
	LatestMoviesUseCaseImplTest::class,
	LatestMoviesViewModelTest::class
)
class LatestMoviesTestSuite