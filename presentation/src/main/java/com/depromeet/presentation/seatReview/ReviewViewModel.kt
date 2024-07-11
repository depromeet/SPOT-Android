package com.depromeet.presentation.seatReview

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import javax.inject.Inject

class ReviewViewModel @Inject constructor() : ViewModel() {
    private val _selectedDate = MutableLiveData<String>()
    val selectedDate: LiveData<String> = _selectedDate

    fun updateSelectedDate(date: String) {
        _selectedDate.value = date
    }
}
