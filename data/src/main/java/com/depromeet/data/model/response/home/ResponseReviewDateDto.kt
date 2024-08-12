package com.depromeet.data.model.response.home

import com.depromeet.domain.entity.response.home.ResponseReviewDate
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
        fun ResponseReviewDateDto.toReviewDateResponse(): ResponseReviewDate {
            val groupedByYear = this.yearMonths
                .groupBy { it.year }
                .toSortedMap(reverseOrder())

            val firstYear = groupedByYear.keys.firstOrNull()

            val yearMonths = groupedByYear.map { (year, dates) ->
                ResponseReviewDate.YearMonths(
                    year = year,
                    months = buildList {
                        add(ResponseReviewDate.MonthData(month = 0, isClicked = true))
                        addAll(
                            dates.map { date ->
                                ResponseReviewDate.MonthData(
                                    month = date.month,
                                    isClicked = false
                                )
                            }.sortedBy { it.month }
                        )
                    },
                    isClicked = year == firstYear
                )
            }

            return ResponseReviewDate(yearMonths = yearMonths)
        }
    }
}
