package com.dpm.presentation.seatrecord.uistate

sealed interface EditableUiState<out T> {
    object Empty : EditableUiState<Nothing>
    object Loading : EditableUiState<Nothing>

    interface DataState<out T> : EditableUiState<T> {
        val data: T
    }

    data class Success<T>(
        override val data: T,
    ) : DataState<T>

    data class Edit<T>(
        override val data: T,
    ) : DataState<T>

    data class Failure(
        val message: String,
    ) : EditableUiState<Nothing>


}
