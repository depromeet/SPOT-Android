package com.depromeet.data.model.response.home

import com.depromeet.domain.entity.response.home.ProfileResponse
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class ResponseProfileDto(
    //TODO : 서버에서 팀 아이디 받아와서 넘겨줘야함
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
        fun ResponseProfileDto.toProfileResponse() = ProfileResponse(
            profileImage = profileImageUrl ?: "",
            nickname = nickname,
            level = level,
            levelTitle = levelTitle,
            teamImage = teamImageUrl ?: "",
        )
    }
}
