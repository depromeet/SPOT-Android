package com.depromeet.presentation.seatrecord.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.depromeet.presentation.seatrecord.mockdata.RecordDetailMockData
import com.depromeet.presentation.seatrecord.mockdata.makeSeatRecordData
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import javax.inject.Inject

@HiltViewModel
class SeatRecordViewModel @Inject constructor() : ViewModel() {

    private val _uiState = MutableStateFlow(RecordDetailMockData())
    val uiState = _uiState.asStateFlow()

    private val _selectedMonth = MutableStateFlow(0)
    val selectedMonth = _selectedMonth.asStateFlow()

    private val _selectedYear = MutableStateFlow(2024)


    fun getSeatRecords() {
        makeSeatRecordData().onEach {
            _uiState.value = _uiState.value.copy(
                profileDetailData = it.profileDetailData,
                reviews = it.reviews
            )
        }.launchIn(viewModelScope)
    }

    fun setSelectedYear(year: Int) {
        _selectedYear.value = year
    }

    fun setSelectedMonth(month: Int) {
        _selectedMonth.value = month
    }

}