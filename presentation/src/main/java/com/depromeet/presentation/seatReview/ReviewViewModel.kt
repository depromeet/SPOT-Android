package com.depromeet.presentation.seatReview

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import javax.inject.Inject

class ReviewViewModel @Inject constructor() : ViewModel() {
    private val _selectedDate = MutableLiveData<String>()
    val selectedDate: LiveData<String> = _selectedDate
    private val _detailReviewText = MutableLiveData<String>()
    val detailReviewText: LiveData<String> get() = _detailReviewText

    private val _selectedButtons = MutableLiveData<List<String>>()
    val selectedButtons: LiveData<List<String>> get() = _selectedButtons

    fun updateSelectedDate(date: String) {
        _selectedDate.value = date
    }

    fun setDetailReviewText(text: String) {
        _detailReviewText.value = text
    }

    fun setSelectedButtons(buttonTexts: List<String>) {
        _selectedButtons.value = buttonTexts
    }
}
