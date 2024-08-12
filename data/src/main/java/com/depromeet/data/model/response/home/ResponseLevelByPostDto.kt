package com.depromeet.data.model.response.home

import com.depromeet.domain.entity.response.home.ResponseLevelByPost
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable


@Serializable
data class ResponseLevelByPostDto(
    @SerialName("level")
    val level : Int,
    @SerialName("title")
    val title : String,
    @SerialName("minimum")
    val minimum : Int,
    @SerialName("maximum")
    val maximum : Int?
) {
    fun toLevelByPostResponse() = ResponseLevelByPost(
        level = level,
        title = title,
        minimum = minimum,
        maximum = maximum
    )
}