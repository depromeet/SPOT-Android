package com.depromeet.data.datasource

import com.depromeet.data.model.response.StadiumResponseDto
import com.depromeet.data.model.response.StadiumsResponseDto

interface ViewfinderDataSource {
    suspend fun getStadiums(): List<StadiumsResponseDto>
    suspend fun getStadium(id: Int): StadiumResponseDto
}