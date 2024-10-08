package com.dpm.data.model.request.signup

import com.dpm.domain.entity.request.signup.PostSignupModel
import kotlinx.serialization.Serializable

@Serializable
data class RequestPostSignupDto(
    val accessToken: String,
    val nickname: String,
    val teamId: Int?
)

fun PostSignupModel.toRequestPostSignupDto(): RequestPostSignupDto {
    return RequestPostSignupDto(
        accessToken = idCode,
        nickname = nickname,
        teamId = teamId.takeIf { it !=0 }
    )
}
