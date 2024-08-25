package com.dpm.data.model.request.home

import com.dpm.domain.entity.request.home.RequestScrap
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class RequestScrapDto(
    @SerialName("stadiumId")
    val stadiumId: Int?,
    @SerialName("months")
    val months: List<Int>?,
    @SerialName("good")
    val good: List<String>?,
    @SerialName("bad")
    val bad: List<String>?,
)

fun RequestScrap.toRequestScrapDto() = RequestScrapDto(
    stadiumId = stadiumId,
    months = months,
    good = good,
    bad = bad
)