package com.dpm.data.model.response.signup

import com.dpm.domain.entity.response.signup.SignupTokenModel
import kotlinx.serialization.Serializable

@Serializable
data class ResponsePostSignupDto(
    val jwtToken: String
)

fun ResponsePostSignupDto.toSignupTokenModel(): SignupTokenModel {
    return SignupTokenModel(
        jwtToken = jwtToken
    )
}
