package com.depromeet.presentation.extension

import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.time.format.TextStyle
import java.util.Locale

const val NICKNAME_PATTERN = "^[a-zA-Z0-9가-힣]+$"

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
    if (parts.size == 3) {
        val month = parts[1].toIntOrNull()?.toString() ?: return ""
        return if (includeSuffixMonth) "${month}월" else month
    } else if (this.length <= 2) {
        return this.toIntOrNull()?.toString() ?: return ""
    }
    return ""
}

//요일 확인
fun String.getDayOfWeek(): String {
    val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd")
    val date = LocalDate.parse(this, formatter)
    return date.dayOfWeek.getDisplayName(TextStyle.SHORT, Locale.KOREAN)
}

fun String.getYearMonthDay(): String {
    return LocalDate.parse(this, DateTimeFormatter.ISO_LOCAL_DATE)
        .format(DateTimeFormatter.ofPattern("yyyy년 M월 d일"))
}