package com.depromeet.data.datasource.remote

import com.depromeet.data.datasource.ViewfinderDataSource
import com.depromeet.data.model.response.viewfinder.StadiumResponseDto
import com.depromeet.data.model.response.viewfinder.StadiumsResponseDto
import com.depromeet.data.remote.ViewfinderService
import javax.inject.Inject

class ViewfinderDataSourceImpl @Inject constructor(
    private val viewfinderService: ViewfinderService
): ViewfinderDataSource {
    override suspend fun getStadiums(): List<StadiumsResponseDto> {
        return viewfinderService.getStadiums()
    }

    override suspend fun getStadium(id: Int): StadiumResponseDto {
        return viewfinderService.getStadium(id)
    }
}