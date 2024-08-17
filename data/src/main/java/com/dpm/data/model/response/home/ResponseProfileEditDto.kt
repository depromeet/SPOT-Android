package com.dpm.data.model.response.home

import com.dpm.domain.entity.response.home.ResponseProfileEdit
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
        fun ResponseProfileEditDto.toProfileEditResponse(): ResponseProfileEdit {
            return ResponseProfileEdit(
                id = id,
                nickname = nickname,
                teamId = teamId
            )
        }
    }

}