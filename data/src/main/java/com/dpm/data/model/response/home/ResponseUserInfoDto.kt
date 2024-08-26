package com.dpm.data.model.response.home

import com.dpm.domain.entity.response.home.ResponseUserInfo
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class ResponseUserInfoDto(
    @SerialName("userId")
    val userId: Int,
    @SerialName("profileImageUrl")
    val profileImageUrl: String?,
    @SerialName("level")
    val level: Int,
    @SerialName("levelTitle")
    val levelTitle: String,
    @SerialName("nickname")
    val nickname: String,
    @SerialName("reviewCount")
    val reviewCount: Int,
    @SerialName("totalLikes")
    val totalLikes: Int,
    @SerialName("teamId")
    val teamId: Int?,
    @SerialName("teamName")
    val teamName: String?,
)

fun ResponseUserInfoDto.toResponseUserInfo() = ResponseUserInfo(
    userId = userId,
    profileImage = profileImageUrl ?: "",
    level = level,
    levelTitle = levelTitle,
    nickname = nickname,
    reviewCount = reviewCount,
    totalLikes = totalLikes,
    teamId = teamId,
    teamName = teamName
)