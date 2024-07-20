package com.depromeet.data.model.request.signup

import com.depromeet.domain.entity.request.signup.PostSignupModel
import kotlinx.serialization.Serializable

@Serializable
data class RequestPostSignupDto(
    val idCode: String,
    val nickname: String,
    val teamId: Int
)

fun PostSignupModel.toRequestPostSignupDto(): RequestPostSignupDto {
    return RequestPostSignupDto(
        idCode = idCode,
        nickname = nickname,
        teamId = teamId
    )
}
