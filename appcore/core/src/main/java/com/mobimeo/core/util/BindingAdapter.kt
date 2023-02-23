package com.mobimeo.core.util

import android.widget.ImageView
import androidx.databinding.BindingAdapter
import com.mobimeo.core.R
import com.mobimeo.core.glide.GlideApp

/**
 * Created By Rafiqul Hasan
 */

@BindingAdapter("image_url")
fun ImageView.loadImage(url: String?) {
	GlideApp.with(this)
		.load(url)
		.placeholder(R.drawable.ic_movie_placeholder)
		.error(R.drawable.ic_movie_placeholder)
		.into(this)
}