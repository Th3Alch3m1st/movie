package com.mobimeo.movies.presentation

import androidx.annotation.VisibleForTesting
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.cachedIn
import com.mobimeo.movies.dto.usecase.LatestMoviesUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.*
import javax.inject.Inject

/**
 * Created by Rafiqul Hasan
 */
@OptIn(ExperimentalCoroutinesApi::class)
@HiltViewModel
class LatestMoviesViewModel @Inject constructor(private val useCase: LatestMoviesUseCase) :
	ViewModel() {

	//To avoid duplication call during orientation change
	private val latestMovieState = MutableSharedFlow<Boolean>()

	private val actionLatestMovie = latestMovieState
		.distinctUntilChanged()
		.onStart { emit(true) }

	val latestMovies = actionLatestMovie.flatMapLatest {
		useCase.getLatestMovies()
	}.cachedIn(viewModelScope)

	//cachedIn throw exception during unit test; cachedIn only for save paging state to survive orientation
	@VisibleForTesting
	val latestMoviesTest = actionLatestMovie.flatMapLatest {
		useCase.getLatestMovies()
	}

}