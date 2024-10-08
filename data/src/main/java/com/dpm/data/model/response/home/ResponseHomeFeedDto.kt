package com.dpm.data.model.response.home

import com.dpm.domain.entity.response.home.ResponseHomeFeed
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class ResponseHomeFeedDto(
    @SerialName("level")
    val level : Int,
    @SerialName("teamName")
    val teamName : String?,
    @SerialName("teamId")
    val teamId : Int?,
    @SerialName("levelTitle")
    val levelTitle : String,
    @SerialName("reviewCntToLevelUp")
    val reviewCntToLevelup : Int,
    @SerialName("mascotImageUrl")
    val mascotImageUrl : String
) {
    fun toHomeFeedResponse() = ResponseHomeFeed(
        level = level,
        teamName = teamName,
        teamId = teamId,
        levelTitle = levelTitle,
        reviewCntToLevelup = reviewCntToLevelup,
        mascotImageUrl = mascotImageUrl
    )
}
