package com.depromeet.presentation.extension

import com.depromeet.presentation.login.viewmodel.NicknameInputState

const val NICKNAME_PATTERN = "^[a-zA-Z0-9가-힣]+$"

fun String.validateNickName(): NicknameInputState {
    return when {
        this.length !in 2..10 -> NicknameInputState.INVALID_LENGTH
        !this.matches(Regex(NICKNAME_PATTERN)) -> NicknameInputState.INVALID_CHARACTER
        else -> NicknameInputState.VALID
    }
}