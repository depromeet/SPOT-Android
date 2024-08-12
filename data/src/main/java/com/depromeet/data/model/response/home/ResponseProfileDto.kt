package com.depromeet.data.model.response.home

import com.depromeet.domain.entity.response.home.ResponseProfile
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class ResponseProfileDto(
    @SerialName("teamId")
    val teamId : Int,
    @SerialName("profileImageUrl")
    val profileImageUrl: String?,
    @SerialName("nickname")
    val nickname: String,
    @SerialName("level")
    val level: Int,
    @SerialName("levelTitle")
    val levelTitle: String,
    @SerialName("teamImageUrl")
    val teamImageUrl: String?,
) {
    companion object {
        fun ResponseProfileDto.toProfileResponse() = ResponseProfile(
            teamId = teamId,
            profileImage = profileImageUrl ?: "",
            nickname = nickname,
            level = level,
            levelTitle = levelTitle,
            teamImage = teamImageUrl ?: "",
        )
    }
}
