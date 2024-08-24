package com.dpm.designsystem

import android.graphics.Paint
import android.view.LayoutInflater
import android.view.View
import androidx.core.content.ContextCompat
import com.depromeet.designsystem.R
import com.depromeet.designsystem.databinding.SpotSnackbarBinding
import com.google.android.material.snackbar.Snackbar

class SpotSnackBar(
    view: View,
    private val snackBarBackground: Int,
    private val message: String,
    private val endMessage: String,
    private val onClick: () -> Unit
) {
    companion object {
        fun make(view: View, background: Int = R.drawable.rect_transfer_black_03_fill_60, message: String = "", endMessage: String = "", onClick: () -> Unit) =
            SpotSnackBar(view = view, snackBarBackground = background, message=  message, endMessage = endMessage, onClick = onClick)
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
            setBackgroundResource(snackBarBackground)
            addView(snackbarBinding.root, 0)
        }
        with(snackbarBinding) {
            tvDescription.text = message
            tvTrigger.paintFlags = Paint.UNDERLINE_TEXT_FLAG
            tvTrigger.text = endMessage
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