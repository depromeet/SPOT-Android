package com.dpm.designsystem

import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import androidx.core.content.ContextCompat
import com.depromeet.designsystem.R
import com.depromeet.designsystem.databinding.SpotImageSnackbarBinding
import com.dpm.designsystem.extension.dpToPx
import com.google.android.material.snackbar.Snackbar

class SpotImageSnackBar(
    view: View,
    private val message: String,
    private val messageColor: Int,
    private val icon: Int,
    private val iconColor: Int,
    private val marginBottom: Int,
) {
    companion object {
        fun make(
            view: View,
            message: String = "",
            messageColor: Int = R.color.white,
            icon: Int = R.drawable.ic_alert_circle,
            iconColor: Int = R.color.white,
            marginBottom: Int = 94,
        ) = SpotImageSnackBar(
            view,
            message,
            messageColor,
            icon,
            iconColor,
            marginBottom
        )
    }

    private val context = view.context
    private val snackbar = Snackbar.make(view, message, Snackbar.LENGTH_SHORT)
    private val snackbarLayout = snackbar.view as Snackbar.SnackbarLayout
    private val binding: SpotImageSnackbarBinding =
        SpotImageSnackbarBinding.inflate(LayoutInflater.from(context))

    init {
        initView()
    }

    private fun initView() {
        with(snackbarLayout) {
            removeAllViews()
            setPadding(0, 0, 0, marginBottom.dpToPx(context))
            setBackgroundColor(ContextCompat.getColor(context, android.R.color.transparent))
            addView(binding.root, 0)

            setOnTouchListener { v, event ->
                if (event.action == MotionEvent.ACTION_UP) {
                    v.performClick()
                    true
                } else {
                    false
                }
            }
        }
        with(binding) {


            tvDescription.text = message
            tvDescription.setTextColor(ContextCompat.getColor(context, messageColor))

            ivIcon.setImageResource(icon)
            ivIcon.setColorFilter(ContextCompat.getColor(context, iconColor))
        }
    }

    fun show() {
        snackbar.show()
    }

}