package com.depromeet.domain.entity.response.home

data class RecentReviewResponse(
    val totalReviewCount : Int = 0,
    val stadiumName : String = "",
    val date : String = "",
    val reviewImages : List<String> = emptyList(),
    val blockName : String = "",
    val seatNumber : Long = 0
)
