package com.depromeet.presentation.seatReview

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import javax.inject.Inject

@HiltViewModel
class ReviewViewModel @Inject constructor() : ViewModel() {
    private val dateFormatter: DateTimeFormatter = DateTimeFormatter.ofPattern("yy.MM.dd")
    private val currentDate: String = LocalDate.now().format(dateFormatter)

    private val _selectedDate = MutableStateFlow(currentDate)
    val selectedDate: StateFlow<String> = _selectedDate

    private val _detailReviewText = MutableStateFlow<String>("")
    val detailReviewText: StateFlow<String> get() = _detailReviewText

    private val _selectedButtons = MutableStateFlow<List<String>>(emptyList())
    val selectedButtons: StateFlow<List<String>> get() = _selectedButtons

    private val _selectedBlock = MutableStateFlow<String>("")
    val selectedBlock: StateFlow<String> get() = _selectedBlock

    private val _selectedColumn = MutableStateFlow<String>("")
    val selectedColumn: StateFlow<String> get() = _selectedColumn

    private val _selectedNumber = MutableStateFlow<String>("")
    val selectedNumber: StateFlow<String> get() = _selectedNumber

    fun updateSelectedDate(date: String) {
        _selectedDate.value = date
    }

    fun setDetailReviewText(text: String) {
        _detailReviewText.value = text
    }

    fun setSelectedButtons(buttonTexts: List<String>) {
        _selectedButtons.value = buttonTexts
    }

    fun setSelectedBlock(block: String) {
        _selectedBlock.value = block
    }

    fun setSelectedColumn(column: String) {
        _selectedColumn.value = column
    }

    fun setSelectedNumber(number: String) {
        _selectedNumber.value = number
    }
}
