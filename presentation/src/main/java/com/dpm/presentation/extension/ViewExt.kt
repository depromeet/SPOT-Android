package com.dpm.presentation.extension

import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import coil.load
import coil.transform.CircleCropTransformation

inline fun View.setOnSingleClickListener(
    delay: Long = 1000L,
    crossinline block: (View) -> Unit,
) {
    var previousClickedTime = 0L
    setOnClickListener { view ->
        val clickedTime = System.currentTimeMillis()
        if (clickedTime - previousClickedTime >= delay) {
            block(view)
            previousClickedTime = clickedTime
        }
    }
}

fun View.setMargins(left: Int, top: Int, right: Int, bottom: Int) {
    if (layoutParams is ViewGroup.MarginLayoutParams) {
        val p = layoutParams as ViewGroup.MarginLayoutParams
        p.setMargins(left, top, right, bottom)
        requestLayout()
    }
}

fun ImageView.loadAndClip(imageUrl: Any) {
    this.load(imageUrl) {
        placeholder(com.depromeet.designsystem.R.drawable.skeleton_gradient)
        error(com.depromeet.designsystem.R.drawable.skeleton_gradient)
    }
    this.clipToOutline = true
}

fun ImageView.loadAndCircle(image: Any) {
    this.load(image) {
        placeholder(com.depromeet.designsystem.R.drawable.skeleton_gradient)
        error(com.depromeet.designsystem.R.drawable.skeleton_gradient)
        transformations(CircleCropTransformation())
    }
    this.clipToOutline = true
}

fun ImageView.loadAndCircleProfile(image: Any) {
    this.load(image) {
        placeholder(com.depromeet.designsystem.R.drawable.skeleton_gradient)
        error(com.depromeet.designsystem.R.drawable.ic_default_profile)
        transformations(CircleCropTransformation())
    }
    this.clipToOutline = true
}
