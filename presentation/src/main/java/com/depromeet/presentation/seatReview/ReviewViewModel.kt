package com.depromeet.presentation.seatReview

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class ReviewViewModel @Inject constructor() : ViewModel() {
    private val _selectedDate = MutableLiveData<String>()
    val selectedDate: LiveData<String> = _selectedDate
    private val _detailReviewText = MutableLiveData<String>()
    val detailReviewText: LiveData<String> get() = _detailReviewText

    private val _selectedButtons = MutableLiveData<List<String>>()
    val selectedButtons: LiveData<List<String>> get() = _selectedButtons

    private val _selectedBlock = MutableLiveData<String>()
    val selectedBlock: LiveData<String> get() = _selectedBlock

    private val _selectedColumn = MutableLiveData<String>()
    val selectedColumn: LiveData<String> get() = _selectedColumn

    private val _selectedNumber = MutableLiveData<String>()
    val selectedNumber: LiveData<String> get() = _selectedNumber
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

    fun setSelctedNumber(number: String) {
        _selectedNumber.value = number
    }
}
