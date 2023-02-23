package com.mobimeo.core.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

/**
 * Created by Rafiqul Hasan
 */
@Parcelize
data class MovieUIModel(
	val id:Int,
	val title:String,
	val releaseDate:String,
	val overView:String,
	val backDropImage:String,
	val thumbnailBackDropImage:String,
	val posterImage:String,
	val genreIds:List<Int>,
	val rating:Float,
	val voteCount:Int
): Parcelable