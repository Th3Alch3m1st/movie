package com.mobimeo.searchmovies.presentation

import android.content.res.Configuration
import android.os.Bundle
import android.view.View
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.navigation.fragment.findNavController
import androidx.paging.LoadState
import androidx.recyclerview.widget.GridLayoutManager
import com.mobimeo.core.fragment.BaseFragment
import com.mobimeo.core.loadstateadapter.PagingLoadStateAdapter
import com.mobimeo.core.model.MovieUIModel
import com.mobimeo.core.util.*
import com.mobimeo.feature.searchmovies.R
import com.mobimeo.feature.searchmovies.databinding.FragmentSearchMoviesBinding
import com.mobimeo.moviedetails.MovieDetailsFragment
import com.mobimeo.searchmovies.presentation.adapter.SearchMoviesAdapter
import dagger.hilt.android.AndroidEntryPoint

/**
 * Created by Rafiqul Hasan
 */
@AndroidEntryPoint
class SearchMoviesFragment : BaseFragment<FragmentSearchMoviesBinding>() {
	private val viewModel: SearchMoviesViewModel by viewModels()

	private var itemDecoration: GridItemDecoration? = null
	private var spanCount = 2

	private lateinit var moviesAdapter: SearchMoviesAdapter

	override val layoutResourceId: Int
		get() = R.layout.fragment_search_movies

	override fun onCreate(savedInstanceState: Bundle?) {
		super.onCreate(savedInstanceState)

		//init recyclerview adapter
		moviesAdapter = SearchMoviesAdapter { movieInfo ->
			navigateToMovieDetails(movieInfo)
		}

		spanCount =
			if (resources.configuration.orientation == Configuration.ORIENTATION_LANDSCAPE) {
				3
			} else {
				2
			}
	}

	override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
		super.onViewCreated(view, savedInstanceState)
		setupToolbar()
		initSearchView()
		initAdapterAndRecyclerView()
		initObserver()
	}

	private fun setupToolbar() {
		dataBinding.toolbar.title = getString(R.string.title_search_movies)
		fragmentCommunicator?.setActionBar(dataBinding.toolbar, true)
	}

	private fun initSearchView() {
		with(dataBinding) {
			searchView.setOnQueryTextListener(
				DebouncingQueryTextListener(
					this@SearchMoviesFragment.lifecycle
				) { newText ->
					viewModel.searchMovies(newText)
				}
			)
			searchView.clearFocus()
		}
	}

	private fun initAdapterAndRecyclerView() {
		with(dataBinding) {
			rvSearchResult.apply {
				layoutManager = GridLayoutManager(context, spanCount)
				itemDecoration?.let {
					removeItemDecoration(it)
				}
				itemDecoration = GridItemDecoration(
					requireContext().resources.getDimension(
						com.intuit.sdp.R.dimen._4sdp
					).toInt(),
					spanCount
				)
				addItemDecoration(itemDecoration!!)
			}

			with(moviesAdapter) {
				rvSearchResult.adapter = withLoadStateFooter(
					footer = PagingLoadStateAdapter(moviesAdapter)
				)
				if (itemCount > 0) {
					viewEmpty.root.gone()
				}
			}
		}
	}

	private fun initObserver() {
		viewModel.searchMovies.launchAndCollectIn(viewLifecycleOwner, Lifecycle.State.STARTED) {
			moviesAdapter.submitData(it)
		}

		moviesAdapter.loadStateFlow.launchAndCollectIn(
			viewLifecycleOwner,
			Lifecycle.State.STARTED
		) { loadState ->
			//for initial loading dialog
			val isLoading = loadState.refresh is LoadState.Loading
			if (isLoading) {
				fragmentCommunicator?.showLoader()
			} else {
				fragmentCommunicator?.hideLoader()
			}

			//check if first page response is empty
			val isListEmpty =
				loadState.refresh is LoadState.NotLoading && moviesAdapter.itemCount == 0
			if (isListEmpty) {
				dataBinding.viewEmpty.root.show()
				dataBinding.viewEmpty.tvTitle.text =
					getString(R.string.msg_nothing_found_search)
				dataBinding.viewEmpty.btnTryAgain.gone()
			} else {
				dataBinding.viewEmpty.root.gone()
			}

			// Show the error result if initial load fails.
			val isInitialLoadOrRefreshFail = loadState.source.refresh is LoadState.Error
			if (isInitialLoadOrRefreshFail) {
				val error = (loadState.refresh as LoadState.Error).error
				showHideErrorUI(
					error.message ?: getString(R.string.msg_unknown_error),
					getString(com.mobimeo.core.R.string.retry)
				) {
					moviesAdapter.retry()
				}
			}
		}

	}

	private fun showHideErrorUI(title: String, buttonText: String, clickListener: () -> Unit) {
		dataBinding.viewEmpty.root.show()
		dataBinding.viewEmpty.tvTitle.text = title
		dataBinding.viewEmpty.btnTryAgain.text = buttonText
		dataBinding.viewEmpty.btnTryAgain.setOnClickListener {
			clickListener.invoke()
		}
	}

	private fun navigateToMovieDetails(movieInfo: MovieUIModel) {
		val bundle = Bundle().apply {
			putParcelable(MovieDetailsFragment.ARG_MOVIE, movieInfo)
		}
		findNavController().navigateSafe(
			R.id.action_fragment_search_movies_to_movie_details,
			bundle
		)
	}
}