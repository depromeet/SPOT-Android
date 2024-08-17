package com.dpm.domain.entity.response.home

data class ResponseBaseballTeam(
    val id : Int,
    val name : String = "",
    val logo : String = "",
    val isClicked : Boolean = false
)
