package com.depromeet.domain.entity.response.home

data class BaseballTeamResponse(
    val id : Int,
    val name : String = "",
    val logo : String = "",
    val isClicked : Boolean = false
)
