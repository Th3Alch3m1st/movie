package com.mobimeo.movies.presentation

import android.content.res.Configuration
import android.os.Bundle
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import androidx.core.view.MenuHost
import androidx.core.view.MenuProvider
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.navigation.fragment.findNavController
import androidx.paging.LoadState
import androidx.recyclerview.widget.GridLayoutManager
import com.mobimeo.core.fragment.BaseFragment
import com.mobimeo.core.loadstateadapter.PagingLoadStateAdapter
import com.mobimeo.core.model.MovieUIModel
import com.mobimeo.core.util.*
import com.mobimeo.feature.latestmovies.R
import com.mobimeo.feature.latestmovies.databinding.FragmentLatestMoviesBinding
import com.mobimeo.moviedetails.MovieDetailsFragment
import com.mobimeo.movies.presentation.adapter.LatestMoviesAdapter
import dagger.hilt.android.AndroidEntryPoint

/**
 * Created by Rafiqul Hasan
 */

@AndroidEntryPoint
class LatestMoviesFragment : BaseFragment<FragmentLatestMoviesBinding>() {
	private val viewModel: LatestMoviesViewModel by viewModels()
	private lateinit var latestMoviesAdapter: LatestMoviesAdapter

	private var itemDecoration: GridItemDecoration? = null
	private var spanCount = 2
	override val layoutResourceId: Int
		get() = R.layout.fragment_latest_movies

	override fun onCreate(savedInstanceState: Bundle?) {
		super.onCreate(savedInstanceState)
		latestMoviesAdapter = LatestMoviesAdapter { movieInfo ->
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
		initToolbar()
		setupMenu()
		initAdapterAndRecyclerView()
		initObserver()
	}

	private fun initToolbar() {
		dataBinding.layoutToolbar.toolbar.title = getString(R.string.title_latest_movies)
		fragmentCommunicator?.setActionBar(dataBinding.layoutToolbar.toolbar, false)
	}

	private fun setupMenu() {
		(requireActivity() as MenuHost).addMenuProvider(object : MenuProvider {
			override fun onPrepareMenu(menu: Menu) {

			}

			override fun onCreateMenu(menu: Menu, menuInflater: MenuInflater) {
				menuInflater.inflate(R.menu.menu, menu)
			}

			override fun onMenuItemSelected(menuItem: MenuItem): Boolean {
				when (menuItem.itemId) {
					R.id.action_search -> {
						findNavController().navigateSafe(R.id.action_fragment_latest_movies_to_search_movies)
					}
					R.id.action_setting -> {
						findNavController().navigateSafe(R.id.action_fragment_latest_movies_to_settings)
					}
					else -> {

					}
				}
				return true
			}
		}, viewLifecycleOwner, Lifecycle.State.RESUMED)
	}

	private fun initAdapterAndRecyclerView() {
		with(dataBinding) {
			rvLatestMovies.apply {
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

			with(latestMoviesAdapter) {
				rvLatestMovies.adapter = withLoadStateFooter(
					footer = PagingLoadStateAdapter(latestMoviesAdapter)
				)
				if (itemCount > 0) {
					viewEmpty.root.gone()
				}
			}
		}
	}

	private fun initObserver() {
		viewModel.latestMovies.launchAndCollectIn(viewLifecycleOwner, Lifecycle.State.STARTED) {
			latestMoviesAdapter.submitData(it)
		}

		latestMoviesAdapter.loadStateFlow.launchAndCollectIn(
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
				loadState.refresh is LoadState.NotLoading && latestMoviesAdapter.itemCount == 0
			if (isListEmpty) {
				dataBinding.viewEmpty.root.show()
				dataBinding.viewEmpty.tvTitle.text = getString(R.string.msg_nothing_found)
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
					latestMoviesAdapter.retry()
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
			R.id.action_fragment_latest_movies_to_movie_details,
			bundle
		)
	}
}