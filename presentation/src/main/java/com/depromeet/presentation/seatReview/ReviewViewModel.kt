package com.depromeet.presentation.seatReview

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import javax.inject.Inject

@HiltViewModel
class ReviewViewModel @Inject constructor() : ViewModel() {
    private val dateFormatter: DateTimeFormatter = DateTimeFormatter.ofPattern("yy.MM.dd")
    private val currentDate: String = LocalDate.now().format(dateFormatter)

    // 시야 후기

    private val _selectedDate = MutableStateFlow(currentDate)
    val selectedDate: LiveData<String> = _selectedDate.asLiveData()

    private val _reviewCount = MutableStateFlow(0)
    val reviewCount: LiveData<Int> = _reviewCount.asLiveData()

    private val _selectedReviewBtn = MutableStateFlow<List<String>>(emptyList())
    val selectedReviewBtn: LiveData<List<String>> = _selectedReviewBtn.asLiveData()

    private val _detailReviewText = MutableStateFlow("")
    val detailReviewText: LiveData<String> = _detailReviewText.asLiveData()

    // 좌석 선택

    private val _selectedSeatName = MutableStateFlow("")
    val selectedSeatName: LiveData<String> = _selectedSeatName.asLiveData()

    private val _selectedBlock = MutableStateFlow("")
    val selectedBlock: LiveData<String> = _selectedBlock.asLiveData()

    private val _selectedColumn = MutableStateFlow("")
    val selectedColumn: LiveData<String> = _selectedColumn.asLiveData()

    private val _selectedNumber = MutableStateFlow("")
    val selectedNumber: LiveData<String> = _selectedNumber.asLiveData()

    // 날짜
    fun updateSelectedDate(date: String) {
        _selectedDate.value = date
    }

    // 시야 후기

    fun setSelectedButtons(buttonTexts: List<String>) {
        _selectedReviewBtn.value = buttonTexts
        Log.d("minju", selectedReviewBtn.value.toString())
    }

    fun setReviewCount(count: Int) {
        _reviewCount.value = count
        Log.d("minju", reviewCount.value.toString())
    }

    fun setDetailReviewText(text: String) {
        _detailReviewText.value = text
        Log.d("minju", detailReviewText.value.toString())
    }

    fun setSelectedSeatName(name: String) {
        _selectedSeatName.value = name
        Log.d("minju", selectedSeatName.value.toString())
    }

    fun setSelectedBlock(block: String) {
        _selectedBlock.value = block
        Log.d("minju", selectedBlock.value.toString())
    }

    fun setSelectedColumn(column: String) {
        _selectedColumn.value = column
        Log.d("minju", selectedColumn.value.toString())
    }

    fun setSelectedNumber(number: String) {
        _selectedNumber.value = number
        Log.d("minju", selectedNumber.value.toString())
    }
}
