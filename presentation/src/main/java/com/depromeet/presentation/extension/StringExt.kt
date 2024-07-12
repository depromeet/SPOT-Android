package com.depromeet.presentation.extension

const val NICKNAME_PATTERN = "^[a-zA-Z0-9가-힣]+$"
const val TEST_DUPLICATE_NICKNAME = "안드로이드" //서버 연동되면 삭제예정

sealed class NickNameError {
    object NoError : NickNameError()
    object LengthError : NickNameError()
    object InvalidCharacterError : NickNameError()
    object DuplicateError : NickNameError()
}


fun String.validateNickName(): NickNameError {
    return when {
        this.length !in 2..10 -> NickNameError.LengthError
        !this.matches(Regex(NICKNAME_PATTERN)) -> NickNameError.InvalidCharacterError
        this == TEST_DUPLICATE_NICKNAME -> NickNameError.DuplicateError
        else -> NickNameError.NoError
    }
}