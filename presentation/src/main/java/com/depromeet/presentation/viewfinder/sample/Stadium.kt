package com.depromeet.presentation.viewfinder.sample

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

/**
 * @DUMMY
 */
@Parcelize
data class Stadium(
    val id: Int,
    val title: String,
    val team: List<String>,
    val imageUrl: String,
    val lock: Boolean
): Parcelable
