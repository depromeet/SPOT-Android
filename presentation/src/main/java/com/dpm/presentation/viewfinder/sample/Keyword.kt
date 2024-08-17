package com.dpm.presentation.viewfinder.sample

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Keyword(
    val message: String,
    val like: Int,
    val type: Int // 0: 좋은 거, 1 : 나쁜 거
): Parcelable
