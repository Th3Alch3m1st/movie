package com.mobimeo.core.util

import android.os.SystemClock
import android.view.View

/**
 * Created By Rafiqul Hasan
 */

class SafeClickListener(
	private var defaultInterval: Int = 1500,
	private val onSafeCLick: (View) -> Unit
) : View.OnClickListener {
	private var lastTimeClicked: Long = 0
	override fun onClick(v: View) {
		if (SystemClock.elapsedRealtime() - lastTimeClicked < defaultInterval) {
			return
		}
		lastTimeClicked = SystemClock.elapsedRealtime()
		onSafeCLick(v)
	}
}

fun View.setSafeOnClickListener(onSafeClick: (View) -> Unit) {
	val safeClickListener = SafeClickListener {
		onSafeClick(it)
	}
	setOnClickListener(safeClickListener)
}