package com.depromeet.domain.preference

interface SharedPreference {
    var token: String
    var refreshToken: String
    var nickname: String
    fun clear()
}
