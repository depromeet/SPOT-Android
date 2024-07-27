package com.depromeet.data.model.response.home

import com.depromeet.domain.entity.response.home.ReviewDateResponse
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class ResponseReviewDateDto(
    @SerialName("yearMonths")
    val yearMonths: List<ResponseDateDto>,
) {
    @Serializable
    data class ResponseDateDto(
        @SerialName("year")
        val year: Int,
        @SerialName("month")
        val month: Int,
    )

    companion object {
        fun ResponseReviewDateDto.toReviewDateResponse(): ReviewDateResponse {
            val groupedByYear = this.yearMonths.groupBy { it.year }
            val yearMonths = groupedByYear.map { (year, dates) ->
                ReviewDateResponse.YearMonths(
                    year = year,
                    months = listOf(0) +  dates.map { it.month }.sorted()
                )
            }.sortedByDescending { it.year }
            return ReviewDateResponse(yearMonths = yearMonths)
        }
    }
}
