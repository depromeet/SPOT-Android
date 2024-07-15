package com.depromeet.presentation.util

import android.graphics.Typeface
import android.text.Spannable
import android.text.SpannableStringBuilder
import android.text.style.RelativeSizeSpan
import android.text.style.StyleSpan

fun applyBoldAndSizeSpan(
    spannableBuilder: SpannableStringBuilder,
    startIndex: Int,
    endIndex: Int,
) {
    spannableBuilder.setSpan(
        StyleSpan(Typeface.BOLD),
        startIndex,
        endIndex,
        Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
    )

    spannableBuilder.setSpan(
        RelativeSizeSpan(1.3f),
        startIndex,
        endIndex,
        Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
    )
}

fun applyBoldSpan(
    spannableBuilder: SpannableStringBuilder,
    startIndex: Int,
    endIndex: Int
) {
    spannableBuilder.setSpan(
        StyleSpan(Typeface.BOLD),
        startIndex,
        endIndex,
        Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
    )
}