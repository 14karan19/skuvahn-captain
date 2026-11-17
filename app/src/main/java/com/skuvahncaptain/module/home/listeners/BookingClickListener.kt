package com.skuvahncaptain.module.home.listeners

import com.skuvahncaptain.module.home.models.BookingsResponseModel

interface BookingClickListener {

    fun onBookingClick(booking: BookingsResponseModel.Booking)
}
