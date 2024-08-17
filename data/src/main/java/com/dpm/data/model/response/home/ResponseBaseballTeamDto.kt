package com.dpm.data.model.response.home

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class ResponseBaseballTeamDto(
    @SerialName("id")
    val id : Int,
    @SerialName("name")
    val name : String,
    @SerialName("logo")
    val logo : String
)
