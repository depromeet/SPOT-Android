package com.dpm.data.model.request.home

import com.dpm.domain.entity.request.home.RequestProfileEdit
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
        fun RequestProfileEdit.toProfileEditRequestDto() = RequestProfileEditDto(
            profileImage = url.ifEmpty { null },
            nickname = nickname.ifEmpty { null },
            teamId = teamId.takeIf { it !=0 }
        )
    }
}