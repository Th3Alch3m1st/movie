package com.mobimeo.searchmovies.presentation

import androidx.annotation.VisibleForTesting
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.cachedIn
import com.mobimeo.movies.dto.usecase.SearchMoviesUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * Created by Rafiqul Hasan
 */

@OptIn(ExperimentalCoroutinesApi::class)
@HiltViewModel
class SearchMoviesViewModel @Inject constructor(private val useCase: SearchMoviesUseCase) :
	ViewModel() {
	companion object {
		const val DEFAULT_QUERY = "action"
	}

	private val searchActionStateFlow = MutableSharedFlow<String>()
	private val searchQueryFlow = searchActionStateFlow
		.map {
			it.trim()
		}.filterNot {
			it.isEmpty()
		}.distinctUntilChanged()
		.onStart { emit(DEFAULT_QUERY) }

	val searchMovies = searchQueryFlow
		.flatMapLatest {
			useCase.searchMovies(it)
		}.cachedIn(viewModelScope)

	//cachedIn throw exception during unit test; cachedIn only for save paging state to survive orientation
	@VisibleForTesting
	val searchMoviesTest = searchQueryFlow
		.flatMapLatest {
			useCase.searchMovies(it)
		}

	fun searchMovies(query: String) {
		viewModelScope.launch {
			searchActionStateFlow.emit(query)
		}
	}
}