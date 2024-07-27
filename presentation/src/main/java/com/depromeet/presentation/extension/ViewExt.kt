package com.depromeet.presentation.extension

import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import coil.load
import com.depromeet.presentation.R

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
    this.load(imageUrl){
        placeholder(R.drawable.ic_image_placeholder)
        error(R.drawable.ic_image_error)
    }
    this.clipToOutline = true
}
