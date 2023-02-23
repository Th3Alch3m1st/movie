package com.mobimeo.moviedetails

import android.os.Bundle
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.internal.runner.junit4.AndroidJUnit4ClassRunner
import com.mobimeo.core.model.MovieUIModel
import com.mobimeo.testutil.uitest.launchFragmentInHiltContainer
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import com.mobimeo.feature.moviedetails.R
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mockito

/**
 * Created by Rafiqul Hasan
 */
@RunWith(AndroidJUnit4ClassRunner::class)
@HiltAndroidTest
class MovieDetailsFragmentTest {
	companion object {
		const val WAIT_TIME = 500L
		private const val RELEASE_DATE = "Release date: 2022-12-14"
		private const val RATING = "Rating: 7.6/10 (28259 reviews)"
	}

	@get:Rule
	var hiltRule = HiltAndroidRule(this)


	lateinit var movieUIModel: MovieUIModel


	@Before
	fun setUp() {
		hiltRule.inject()

		movieUIModel = MovieUIModel(
			100,
			title = "Avatar: The Way of Water",
			releaseDate = "2022-12-14",
			overView = "Set more than a decade after the events of the first film, learn the story of the Sully \n" +
				"2023-02-10 01:19:37.667  6968-6999  Response                com.neugelb.moviedb                  I  │ family (Jake, Neytiri, and their kids), the trouble that follows them, the lengths they go to keep each other \n" +
				"2023-02-10 01:19:37.667  6968-6999  Response                com.neugelb.moviedb                  I  │ safe, the battles they fight to stay alive, and the tragedies they endure.",
			backDropImage = "https://image.tmdb.org/t/p/orignal/s16H6tpK2utvwDtzZ8Qy4qm5Emw.jpg",
			thumbnailBackDropImage = "https://image.tmdb.org/t/p/w500/s16H6tpK2utvwDtzZ8Qy4qm5Emw.jpg",
			posterImage = "https://image.tmdb.org/t/p/original/jRXYjXNq0Cs2TcJjLkki24MLp7u.jpg",
			genreIds = mutableListOf(),
			rating = 7.6f,
			voteCount = 28259

		)
	}

	@Test
	fun display_view_opened() {
		//open fragment
		openMovieDetailBottomSheetFragment()


		Thread.sleep(WAIT_TIME)
		//verifying cases
		onView(withId(R.id.iv_movie))
			.check(matches(isDisplayed()))
		onView(withId(R.id.tv_movie_title))
			.check(matches(isDisplayed()))
		onView(withId(R.id.tv_rating))
			.check(matches(isDisplayed()))
		onView(withId(R.id.tv_release_date))
			.check(matches(isDisplayed()))
		onView(withId(R.id.tv_overview_title))
			.check(matches(isDisplayed()))
		onView(withId(R.id.tv_overview))
			.check(matches(isDisplayed()))
	}

	@Test
	fun test_data_is_set_correctly(){
		//open fragment
		openMovieDetailBottomSheetFragment()

		Thread.sleep(WAIT_TIME)

		//verifying cases
		onView(withId(R.id.tv_movie_title))
			.check(matches(withText(movieUIModel.title)))
		onView(withId(R.id.tv_release_date))
			.check(matches(withText(RELEASE_DATE)))
		onView(withId(R.id.tv_rating))
			.check(matches(withText(RATING)))
		onView(withId(R.id.tv_overview))
			.check(matches(withText(movieUIModel.overView)))
	}

	private fun openMovieDetailBottomSheetFragment() {
		val bundle = Bundle().apply {
			putParcelable(
				MovieDetailsFragment.ARG_MOVIE,
				movieUIModel
			)

		}
		val mockNavController = Mockito.mock(NavController::class.java)
		launchFragmentInHiltContainer<MovieDetailsFragment>(
			bundle,
			com.mobimeo.themes.R.style.BottomSheetDialog
		) {
			viewLifecycleOwnerLiveData.observeForever { viewLifecycleOwner ->
				if (viewLifecycleOwner != null) {
					Navigation.setViewNavController(requireView(), mockNavController)
				}
			}
		}
	}
}