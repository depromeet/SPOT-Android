package com.depromeet.presentation.viewfinder.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import javax.inject.Inject

@HiltViewModel
class StadiumDetailViewModel @Inject constructor() : ViewModel() {
    private val _scrollState = MutableStateFlow(false)
    val scrollState = _scrollState

    fun updateScrollState(state: Boolean) {
        _scrollState.value = state
    }

}