package com.depromeet.presentation.viewfinder.sample

data class Review(
    val count: Int,
    val reviewContents: List<ReviewContent>
)

data class ReviewContent(
    val profile: String,
    val user: String,
    val level: Int,
    val seat: String,
    val date: String,
    val images: List<String>,
    val content: String,
    val keyword: List<Keyword>
)
