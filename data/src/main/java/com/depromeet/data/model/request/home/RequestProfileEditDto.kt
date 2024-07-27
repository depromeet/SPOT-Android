package com.depromeet.data.model.request.home

import com.depromeet.domain.entity.request.home.ProfileEditRequest
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class RequestProfileEditDto(
    @SerialName("profileImage")
    val profileImage: String? = null,
    @SerialName("nickname")
    val nickname: String? = null,
    @SerialName("teamId")
    val teamId: Int? = null,
) {
    companion object {
        fun ProfileEditRequest.toProfileEditRequestDto() = RequestProfileEditDto(
            profileImage = url.ifEmpty { null },
            nickname = nickname.ifEmpty { null },
            teamId = teamId.takeIf { it !=0 }
        )
    }
}