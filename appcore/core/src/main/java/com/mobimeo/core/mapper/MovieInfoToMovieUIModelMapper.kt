package com.mobimeo.core.mapper

import com.mobimeo.core.model.MovieUIModel
import com.mobimeo.core.model.MovieInfo
import com.mobimeo.core.BuildConfig

/**
 * Created by Rafiqul Hasan
 */
class MovieInfoToMovieUIModelMapper() : Mapper<MovieInfo, MovieUIModel> {
	override fun map(input: MovieInfo): MovieUIModel {
		return MovieUIModel(
			id = input.id ?: 0,
			title = input.title ?: "",
			releaseDate = input.releaseDate ?: "",
			overView = input.overview ?: "",
			backDropImage = "${BuildConfig.IMAGE_URL}/original/${input.backdropPath ?: ""}",
			thumbnailBackDropImage = "${BuildConfig.IMAGE_URL}/w500/${input.backdropPath ?: if (!input.posterPath.isNullOrEmpty()) input.posterPath else ""}",
			posterImage = "${BuildConfig.IMAGE_URL}/original/${input.posterPath ?: ""}",
			genreIds = input.genreIds ?: listOf(),
			rating = input.voteAverage ?: 0.0f,
			voteCount = input.voteCount ?: 0
		)
	}
}