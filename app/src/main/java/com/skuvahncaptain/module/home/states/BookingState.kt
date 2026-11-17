package com.skuvahncaptain.module.home.states

import com.skuvahncaptain.module.home.models.BookingsResponseModel

sealed class BookingState {
    object Loading : BookingState()

    data class Success(val list: List<BookingsResponseModel.Booking>) : BookingState()

    data class UpdateSuccess(val message: String) : BookingState()

    data class Error(val message: String) : BookingState()
}