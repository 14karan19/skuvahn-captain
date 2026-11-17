package com.skuvahncaptain.module.home.viewmodels

import androidx.lifecycle.*
import com.skuvahncaptain.base.api.ApiInterface
import com.skuvahncaptain.module.home.states.BookingState
import com.skuvahncaptain.module.home.states.RouteState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class BookingViewModel @Inject constructor(
    private val api: ApiInterface
) : ViewModel() {

    val state = MutableLiveData<BookingState>()

    fun getBookings(token:String, userId: String) = viewModelScope.launch {

        state.value = BookingState.Loading

        try {
            val response = api.getBookings(token, userId)

            if (response.isSuccessful) {

                val body = response.body()

                if (body?.success == true) {
                    state.value = BookingState.Success(body.data ?: emptyList())
                } else {
                    state.value = BookingState.Error(body?.message ?: "Failed")
                }

            } else {
                state.value = BookingState.Error("Error ${response.code()}")
            }

        } catch (e: Exception) {
            state.value = BookingState.Error(e.message ?: "Something went wrong")
        }
    }

    fun updateBooking(token:String, bookingId: String, userId: String, status: Int) = viewModelScope.launch {

        state.value = BookingState.Loading

        try {
            val response = api.updateBooking(token, bookingId, userId, status)

            if (response.isSuccessful) {
                val body = response.body()

                if (body?.success == true && body.data == true) {
                    state.value = BookingState.UpdateSuccess(
                        body.message ?: "Updated"
                    )
                } else {
                    state.value = BookingState.Error(body?.message ?: "Update failed")
                }

            } else {
                state.value = BookingState.Error("Error ${response.code()}")
            }

        } catch (e: Exception) {
            state.value = BookingState.Error(e.message ?: "Error")
        }
    }
}