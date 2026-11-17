package com.skuvahncaptain.module.home.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.skuvahncaptain.databinding.ItemBookingsBinding
import com.skuvahncaptain.module.home.listeners.BookingClickListener
import com.skuvahncaptain.module.home.models.BookingsResponseModel

class BookingsAdapter(
    val context: Context,
    private val bookings: ArrayList<BookingsResponseModel.Booking>
) : RecyclerView.Adapter<BookingsViewHolder>() {

    var bookingClickListener: BookingClickListener? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =
        BookingsViewHolder(
            ItemBookingsBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )

    override fun onBindViewHolder(holder: BookingsViewHolder, position: Int) {

        val item = bookings[position]

        holder.binding.btnViewDetails.setOnClickListener {
            bookingClickListener?.onBookingClick(item)
        }

        holder.bind(item)
    }

    override fun getItemCount() = bookings.size

    fun updateData(newList: List<BookingsResponseModel.Booking>) {
        bookings.clear()
        bookings.addAll(newList)
        notifyDataSetChanged()
    }
}

class BookingsViewHolder(val binding: ItemBookingsBinding) :
    RecyclerView.ViewHolder(binding.root) {

    fun bind(booking: BookingsResponseModel.Booking) {
        binding.booking = booking
    }
}