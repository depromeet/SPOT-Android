package com.depromeet.presentation.extension

import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.time.format.TextStyle
import java.util.Locale

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

/** ex) 2024-01-21 (서버 date형식 예정)*/
fun String.extractDay(): String {
    val parts = this.split("-")
    return if (parts.size == 3) {
        parts[2]
    } else {
        ""
    }
}

fun String.extractMonth(includeSuffixMonth: Boolean): String {
    val parts = this.split("-")
    return if (parts.size == 3) {
        val month = parts[1].toInt().toString()
        if (includeSuffixMonth) "${month}월" else month
    } else {
        ""
    }
}

//요일 확인
fun String.getDayOfWeek(): String {
    val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd")
    val date = LocalDate.parse(this, formatter)
    return date.dayOfWeek.getDisplayName(TextStyle.SHORT, Locale.KOREAN)
}