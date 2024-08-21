package com.dpm.presentation.util

import android.content.Context
import android.text.SpannableStringBuilder
import android.text.Spanned
import android.text.style.ForegroundColorSpan
import androidx.annotation.ColorRes
import androidx.core.content.ContextCompat

class SpannableStringUtils(
    private val context: Context
) {
    /**
     * @param
     * [color] 텍스트에 입힐 색상
     * [text] 적용한 텍스트
     * [start] 텍스트 문자열 시작 지점
     * [end] 텍스트 문자열 마지막 지점 + 1
     */
    fun toColorSpan(
        @ColorRes color: Int,
        text: CharSequence,
        start: Int,
        end: Int,
    ): SpannableStringBuilder {
        val span = SpannableStringBuilder(text)
        span.setSpan(
            ForegroundColorSpan(ContextCompat.getColor(context, color)),
            start,
            end,
            Spanned.SPAN_EXCLUSIVE_EXCLUSIVE
        )
        return span
    }

    fun toColorSpanMulti(
        @ColorRes color: Int,
        text: CharSequence,
        ranges: List<Pair<Int, Int>>
    ): SpannableStringBuilder {
        val span = SpannableStringBuilder(text)
        ranges.forEach { range ->
            span.setSpan(
                ForegroundColorSpan(ContextCompat.getColor(context, color)),
                range.first,
                range.second,
                Spanned.SPAN_EXCLUSIVE_EXCLUSIVE
            )
        }

        return span
    }
}