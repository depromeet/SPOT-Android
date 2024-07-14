package com.depromeet.presentation.seatrecord.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import javax.inject.Inject

@HiltViewModel
class SeatRecordViewModel @Inject constructor() : ViewModel() {

    private val _selectedMonth = MutableStateFlow(0)
    val selectedMonth = _selectedMonth.asLiveData()

    private val _selectedYear = MutableStateFlow(2024)
    val selectedYear = _selectedYear.asLiveData()


}