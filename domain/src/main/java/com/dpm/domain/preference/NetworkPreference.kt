package com.dpm.domain.preference

interface SharedPreference {
    var token: String
    var refreshToken: String
    var level : Int
    var teamName : String
    var teamId : Int
    var levelTitle : String
    var nickname: String
    var profileImage : String
    var isFirstTime: Boolean
    var isFirstShare: Boolean
    var isFirstLike: Boolean
    fun clear()
}
