package com.depromeet.data.model.response.home

import com.depromeet.domain.entity.response.home.ResponseLevelUpInfo
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class ResponseLevelUpInfoDto(
    @SerialName("title")
    val title : String,
    @SerialName("levelUpImage")
    val levelUpImage : String
) {
    fun toLevelUpInfoResponse() = ResponseLevelUpInfo(
        title = title,
        levelUpImage = levelUpImage
    )
}
