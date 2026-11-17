package com.skuvahncaptain.module.home.activities

import android.annotation.SuppressLint
import android.os.Build
import android.view.View
import androidx.activity.viewModels
import androidx.annotation.RequiresApi
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.media3.common.MediaItem
import androidx.media3.exoplayer.ExoPlayer
import com.bumptech.glide.Glide
import com.skuvahncaptain.R
import com.skuvahncaptain.base.view.BaseActivity
import com.skuvahncaptain.databinding.ActivityBookingDetailsBinding
import com.skuvahncaptain.module.home.models.BookingsResponseModel
import com.skuvahncaptain.module.home.states.BookingState
import com.skuvahncaptain.module.home.viewmodels.BookingViewModel
import com.skuvahncaptain.utilities.CustomToast
import dagger.hilt.android.AndroidEntryPoint
import java.text.SimpleDateFormat
import java.util.*
import kotlin.getValue

@AndroidEntryPoint
class BookingDetailsActivity : BaseActivity<ActivityBookingDetailsBinding>() {

    private val viewModel: BookingViewModel by viewModels()
    private var player: ExoPlayer? = null
    private var booking: BookingsResponseModel.Booking? = null

    private var status = 0

    override fun getLayoutResourceId() = R.layout.activity_booking_details

    @RequiresApi(Build.VERSION_CODES.O)
    @SuppressLint("SetTextI18n")
    override fun initView() {
        super.initView()

        window.statusBarColor = getColor(R.color.light_blue)

        booking = intent.getSerializableExtra("booking") as? BookingsResponseModel.Booking

        status = booking?.status!!

        observe()

        initData()
        attachClicks()
    }

    // ---------------- DATA ----------------
    private fun initData() {

        booking?.let { b ->

            when (b.status) {
                0 -> {
                    binding.btnTrack.text = "Accept Booking"
                    binding.playerView.visibility = View.GONE
                }
                1 -> {
                    binding.playerView.visibility = View.GONE
                    binding.btnTrack.text = "End Booking"
                }
                else -> {
                    binding.cvTrack.visibility = View.GONE
                    binding.playerView.visibility = View.GONE
                }
            }

            // CHILD
            binding.tvChildName.text = b.child.fullName
            binding.tvChildDob.text = formatDate(b.child.dob)

            Glide.with(this)
                .load(b.child.imageUrl)
                .placeholder(R.drawable.ic_user_placeholder)
                .into(binding.imgProfile)

            // CHILD DETAILS
            binding.tvChildSchool.text = b.school.name
            binding.tvGrade.text = b.child.grade
            binding.tvEmergencyName.text = b.child.emergencyContactName
            binding.tvEmergencyPhone.text = b.child.emergencyContactPhone

            // BOOKING DETAILS
            binding.tvAddress.text = b.address.fullAddress
            binding.tvSchool.text = b.school.name
            binding.tvDate.text = formatDate(b.createdAt)

            // DRIVER / VEHICLE
            binding.tvDriverName.text = b.captain?.fullName ?: "Not Assigned"
            binding.tvDriverPhone.text = b.captain?.phone ?: "-"

            binding.tvVehicleNumber.text =
                b.vehicle?.registrationNumber ?: "Vehicle Not Assigned"

            Glide.with(this)
                .load(b.captain?.imageUrl)
                .placeholder(R.drawable.ic_user_placeholder)
                .into(binding.imgDriverProfile)
        }
    }

    private fun observe() {

        viewModel.state.observe(this) {

            when (it) {

                is BookingState.Loading -> {
                }

                is BookingState.UpdateSuccess -> {
                    CustomToast.showMessage(this, it.message)
                    status = status++

                    when (status) {
                        0 -> {
                            binding.btnTrack.text = "Accept Booking"
                            binding.playerView.visibility = View.GONE
                        }
                        1 -> {
                            binding.playerView.visibility = View.GONE
                            binding.btnTrack.text = "End Ride"
                        }
                        else -> {
                            binding.cvTrack.visibility = View.GONE
                            binding.playerView.visibility = View.GONE
                        }
                    }
                }

                is BookingState.Error -> {
                    CustomToast.showMessage(this, it.message)
                }

                else -> {}
            }
        }
    }

    // ---------------- PLAYER ----------------
    private fun initializePlayer() {
        player = ExoPlayer.Builder(this).build()
        binding.playerView.player = player

        val mediaItem = MediaItem.fromUri("https://www.example.com/video.mp4")
        player?.setMediaItem(mediaItem)

        player?.prepare()
        player?.playWhenReady = false
    }

    // ---------------- CLICKS ----------------
    private fun attachClicks() {

        binding.ivBack.setOnClickListener {
            finish()
        }

        binding.btnTrack.setOnClickListener {
            if (status == 0) {
                viewModel.updateBooking(pref.token, booking?.id!!, pref.userId,1)
            }else if (status == 1) {
                viewModel.updateBooking(pref.token, booking?.id!!, pref.userId,2)
            }
        }
    }

    // ---------------- FORMAT DATE ----------------
    private fun formatDate(date: String?): String {
        return try {
            val input = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss", Locale.getDefault())
            val output = SimpleDateFormat("dd MMM yyyy", Locale.getDefault())
            val parsed = input.parse(date ?: "")
            output.format(parsed ?: Date())
        } catch (e: Exception) {
            "-"
        }
    }

    // ---------------- LIFECYCLE ----------------
    override fun onStart() {
        super.onStart()
        initializePlayer()
    }

    override fun onStop() {
        super.onStop()
        releasePlayer()
    }

    private fun releasePlayer() {
        player?.release()
        player = null
    }
}