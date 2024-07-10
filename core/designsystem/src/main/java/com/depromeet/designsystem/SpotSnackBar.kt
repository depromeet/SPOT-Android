package com.depromeet.designsystem

import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import androidx.core.content.ContextCompat
import com.depromeet.designsystem.databinding.SpotSnackbarBinding
import com.google.android.material.snackbar.Snackbar

class SpotSnackBar(
    view: View,
    message: String,
    private val onClick: () -> Unit
) {
    companion object {
        fun make(view: View, message: String = "", onClick: () -> Unit) =
            SpotSnackBar(view, message, onClick)
    }

    private val context = view.context
    private val snackbar = Snackbar.make(view, message, Snackbar.LENGTH_SHORT)
    private val snackbarLayout = snackbar.view as Snackbar.SnackbarLayout
    private val snackbarBinding: SpotSnackbarBinding =
        SpotSnackbarBinding.inflate(LayoutInflater.from(context))

    init {
        initView()
        initEvent()
    }

    private fun initView() {
        with(snackbarLayout) {
            removeAllViews()
            setPadding(0, 0, 0, 0)
            setBackgroundColor(ContextCompat.getColor(context, android.R.color.transparent))
            addView(snackbarBinding.root, 0)
        }
    }

    private fun initEvent() {
        snackbarBinding.tvTrigger.setOnClickListener {
            dismiss()
            onClick()
        }
    }

    fun show() {
        snackbar.show()
    }

    fun dismiss() {
        snackbar.dismiss()
    }
}