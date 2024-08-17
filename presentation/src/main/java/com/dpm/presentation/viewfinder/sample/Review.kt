package com.dpm.presentation.viewfinder.sample

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

data class Review(
    val count: Int,
    val reviewContents: List<ReviewContent>
)

@Parcelize
data class ReviewContent(
    val profile: String,
    val user: String,
    val level: Int,
    val seat: String,
    val date: String,
    val images: List<String>,
    val content: String,
    val keyword: List<Keyword>
): Parcelable
