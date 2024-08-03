package com.depromeet.data.model.response.home

import com.depromeet.domain.entity.response.home.ProfileEditResponse
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class ResponseProfileEditDto(
    @SerialName("id")
    val id: Int,
    @SerialName("nickname")
    val nickname: String,
    @SerialName("teamId")
    val teamId: Int?,
) {
    companion object {
        fun ResponseProfileEditDto.toProfileEditResponse(): ProfileEditResponse {
            return ProfileEditResponse(
                id = id,
                nickname = nickname,
                teamId = teamId
            )
        }
    }

}