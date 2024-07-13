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

    private val _selectedDate = MutableStateFlow(currentDate)
    val selectedDate: LiveData<String> = _selectedDate.asLiveData()

    private val _detailReviewText = MutableStateFlow("")
    val detailReviewText: LiveData<String> = _detailReviewText.asLiveData()

    private val _selectedReviewBtn = MutableStateFlow<List<String>>(emptyList())
    val selectedReviewBtn: LiveData<List<String>> = _selectedReviewBtn.asLiveData()

    private val _selectedBlock = MutableStateFlow("")
    val selectedBlock: LiveData<String> = _selectedBlock.asLiveData()

    private val _selectedColumn = MutableStateFlow("")
    val selectedColumn: LiveData<String> = _selectedColumn.asLiveData()

    private val _selectedNumber = MutableStateFlow("")
    val selectedNumber: LiveData<String> = _selectedNumber.asLiveData()

    fun updateSelectedDate(date: String) {
        _selectedDate.value = date
    }

    fun setDetailReviewText(text: String) {
        _detailReviewText.value = text
    }

    fun setSelectedButtons(buttonTexts: List<String>) {
        _selectedReviewBtn.value = buttonTexts
    }

    fun setSelectedBlock(block: String) {
        _selectedBlock.value = block
    }

    fun setSelectedColumn(column: String) {
        _selectedColumn.value = column)
    }

    fun setSelectedNumber(number: String) {
        _selectedNumber.value = number
    }
}
