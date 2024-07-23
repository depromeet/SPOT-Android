package com.depromeet.data.model.response.home

import com.depromeet.domain.entity.response.home.ProfileResponse
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class ResponseProfileDto(
    @SerialName("profileImageUrl")
    val profileImageUrl: String,
    @SerialName("nickname")
    val nickname: String,
    @SerialName("level")
    val level: Int,
    @SerialName("teamImageUrl")
    val teamImageUrl: String,
) {
    companion object {
        fun ResponseProfileDto.toProfileResponse() = ProfileResponse(
            profileImage = profileImageUrl,
            nickname = nickname,
            level = level,
            teamImage = teamImageUrl
        )
    }
}
