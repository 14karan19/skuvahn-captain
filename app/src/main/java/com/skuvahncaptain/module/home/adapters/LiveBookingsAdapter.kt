package com.skuvahncaptain.module.home.adapters

import android.content.Context
import android.os.Build
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.annotation.NonNull
import androidx.annotation.RequiresApi
import androidx.recyclerview.widget.RecyclerView
import com.skuvahncaptain.databinding.ItemLiveBookingsBinding
import com.skuvahncaptain.module.home.listeners.BookingClickListener
import com.skuvahncaptain.module.home.models.BookingsResponseModel
import com.skuvahncaptain.module.home.models.HomeResponseModel

class LiveBookingsAdapter(
    val context: Context,
    private val bookings: ArrayList<BookingsResponseModel.Booking>
) : RecyclerView.Adapter<LiveBookingsViewHolder>() {

    var bookingClickListener: BookingClickListener? = null

    @NonNull
    override fun onCreateViewHolder(@NonNull parent: ViewGroup, viewType: Int): LiveBookingsViewHolder =
        LiveBookingsViewHolder(
            ItemLiveBookingsBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )

    @RequiresApi(Build.VERSION_CODES.N)
    override fun onBindViewHolder(holder: LiveBookingsViewHolder, position: Int) {
        holder.apply {
            binding.btnTrack.setOnClickListener {
                bookingClickListener!!.onBookingClick(bookings[position])
            }

            bind(bookings[position])
        }
    }

    override fun getItemCount(): Int {
        return bookings.size
    }

}

class LiveBookingsViewHolder(val binding: ItemLiveBookingsBinding) : RecyclerView.ViewHolder(binding.root) {

    fun bind(booking: BookingsResponseModel.Booking) {
        binding.booking = booking
    }

}