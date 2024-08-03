package com.depromeet.presentation.seatrecord.paging

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.depromeet.domain.entity.request.home.MySeatRecordRequest
import com.depromeet.domain.entity.response.home.MySeatRecordResponse
import com.depromeet.domain.repository.HomeRepository

class SeatRecordPagingSource(
    private val homeRepository: HomeRepository,
    private val year: Int,
    private val month: Int,
) : PagingSource<Int, MySeatRecordResponse.ReviewResponse>() {

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, MySeatRecordResponse.ReviewResponse> {
        val position = params.key ?: 0

        return try {
            val response = homeRepository.getMySeatRecord(
                MySeatRecordRequest(
                    page = position,
                    size = 10,
                    year = year,
                    month = month.takeIf { it != 0 }
                )
            )
            response.fold(
                onSuccess = { data ->
                    val seatRecords = data.reviews

                    LoadResult.Page(
                        data = seatRecords,
                        prevKey = if (position == 0) null else position - 1,
                        nextKey = if (seatRecords.isEmpty()) null else position + 1
                    )
                },
                onFailure = { exception ->
                    LoadResult.Error(exception)
                }
            )
        } catch (e: Exception) {
            LoadResult.Error(e)
        }
    }

    override fun getRefreshKey(state: PagingState<Int, MySeatRecordResponse.ReviewResponse>): Int? {
        return state.anchorPosition?.let { state.closestPageToPosition(it)?.prevKey?.plus(1) }
    }
}