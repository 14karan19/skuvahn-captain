package com.skuvahncaptain.module.home.activities

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Build
import android.view.View
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.annotation.RequiresApi
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.skuvahncaptain.R
import com.skuvahncaptain.base.view.BaseActivity
import com.skuvahncaptain.databinding.ActivityBookingsBinding
import com.skuvahncaptain.module.home.adapters.BookingsAdapter
import com.skuvahncaptain.module.home.listeners.BookingClickListener
import com.skuvahncaptain.module.home.models.BookingsResponseModel
import com.skuvahncaptain.module.home.states.BookingState
import com.skuvahncaptain.module.home.viewmodels.BookingViewModel
import com.skuvahncaptain.utilities.CustomToast
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class BookingsActivity : BaseActivity<ActivityBookingsBinding>(),
    BookingClickListener {

    private val viewModel: BookingViewModel by viewModels()

    private lateinit var bookingsAdapter: BookingsAdapter

    override fun getLayoutResourceId() = R.layout.activity_bookings

    @RequiresApi(Build.VERSION_CODES.O)
    @SuppressLint("SetTextI18n")
    override fun initView() {
        super.initView()

        setupRecycler()
        attachClicks()
        observe()

        // 🔥 API CALL
        viewModel.getBookings(pref.token, pref.userId)
    }

    // ---------------------------------------
    // Recycler Setup
    // ---------------------------------------
    private fun setupRecycler() {

        bookingsAdapter = BookingsAdapter(this, arrayListOf())
        bookingsAdapter.bookingClickListener = this

        binding.rvBookings.apply {
            layoutManager = LinearLayoutManager(this@BookingsActivity)
            setHasFixedSize(true)
            adapter = bookingsAdapter
        }
    }

    // ---------------------------------------
    // Clicks
    // ---------------------------------------
    private fun attachClicks() {

        binding.ivBack.setOnClickListener {
            finish()
        }

        binding.swipeRefreshLayout.setOnRefreshListener {
            viewModel.getBookings(pref.token, pref.userId)
        }
    }

    // ---------------------------------------
    // Observer
    // ---------------------------------------
    private fun observe() {

        viewModel.state.observe(this) {

            when (it) {

                is BookingState.Loading -> {
                    binding.swipeRefreshLayout.isRefreshing = true
                }

                is BookingState.Success -> {
                    binding.swipeRefreshLayout.isRefreshing = false

                    if (it.list.isEmpty()) {
                        binding.rvBookings.visibility = View.GONE
                        binding.tvNoBookings.visibility = View.VISIBLE
                    } else {
                        binding.rvBookings.visibility = View.VISIBLE
                        binding.tvNoBookings.visibility = View.GONE

                        bookingsAdapter.updateData(it.list)
                    }
                }

                is BookingState.Error -> {
                    binding.swipeRefreshLayout.isRefreshing = false
                    CustomToast.showMessage(this, it.message)
                }

                else -> {}
            }
        }
    }

    // ---------------------------------------
    // Adapter Callback
    // ---------------------------------------
    override fun onBookingClick(booking: BookingsResponseModel.Booking) {
        val intent = Intent(this, BookingDetailsActivity::class.java)
        intent.putExtra("booking", booking)
        startActivity(intent)
    }
}