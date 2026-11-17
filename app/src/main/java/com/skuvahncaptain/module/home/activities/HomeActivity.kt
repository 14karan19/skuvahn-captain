package com.skuvahncaptain.module.home.activities

import android.content.Intent
import android.os.Handler
import android.os.Looper
import android.view.View
import android.widget.LinearLayout
import androidx.activity.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.viewpager2.widget.ViewPager2
import com.skuvahncaptain.R
import com.skuvahncaptain.base.view.BaseActivity
import com.skuvahncaptain.databinding.ActivityHomeBinding
import com.skuvahncaptain.module.home.adapters.BannerAdapter
import com.skuvahncaptain.module.home.adapters.LiveBookingsAdapter
import com.skuvahncaptain.module.home.listeners.BookingClickListener
import com.skuvahncaptain.module.home.models.BookingsResponseModel
import com.skuvahncaptain.module.home.states.HomeState
import com.skuvahncaptain.module.home.viewmodels.HomeViewModel
import com.skuvahncaptain.utilities.CustomToast
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class HomeActivity : BaseActivity<ActivityHomeBinding>(), BookingClickListener {

    private val viewModel: HomeViewModel by viewModels()

    private lateinit var bannerAdapter: BannerAdapter
    private lateinit var liveBookingsAdapter: LiveBookingsAdapter

    private val handler = Handler(Looper.getMainLooper())
    private var currentPage = 0

    override fun getLayoutResourceId() = R.layout.activity_home

    override fun initView() {
        super.initView()

        initBanner()
        attachClicks()
        observe()

        viewModel.getHome(pref.token, pref.userId)
    }

    // ---------------- OBSERVE ----------------
    private fun observe() {

        viewModel.state.observe(this) {

            when (it) {

                is HomeState.Loading -> {
                    binding.swipeRefreshLayout.isRefreshing = true
                }

                is HomeState.Success -> {
                    binding.swipeRefreshLayout.isRefreshing = false
                    val data = it.data

                    // -------- USER --------

                    pref.phoneNumber = data.user?.phone ?: ""
                    pref.email = data.user?.email ?: ""
                    pref.profilePic = data.user?.imageUrl ?: ""
                    pref.name = data.user?.fullName ?: ""

                    binding.tvGreeting.text =
                        "Good Morning, ${data.user?.fullName ?: ""}"

                    // -------- BANNERS --------
                    val banners = data.banners ?: emptyList()

                    bannerAdapter.submitList(banners)

                    currentPage = 0
                    binding.bannerViewPager.setCurrentItem(0, false)

                    initDots(0)

                    if (banners.size > 1) {
                        startAutoScroll()
                    }

                    // -------- BOOKINGS --------
                    val bookings = data.activeBookings ?: emptyList()

                    if (bookings.isEmpty()) {
                        binding.layoutActiveBookingContainer.visibility = View.GONE
                    } else {
                        binding.layoutActiveBookingContainer.visibility = View.VISIBLE

                        liveBookingsAdapter =
                            LiveBookingsAdapter(this, ArrayList(bookings))

                        liveBookingsAdapter.bookingClickListener = this

                        binding.rvActiveBookings.apply {
                            layoutManager = LinearLayoutManager(
                                this@HomeActivity,
                                LinearLayoutManager.HORIZONTAL,
                                false
                            )
                            setHasFixedSize(true)
                            adapter = liveBookingsAdapter
                        }
                    }
                }

                is HomeState.Error -> {
                    binding.swipeRefreshLayout.isRefreshing = false
                    CustomToast.showMessage(this, it.message)
                }

                else -> {}
            }
        }
    }

    // ---------------- BANNER ----------------
    private fun initBanner() {

        bannerAdapter = BannerAdapter()
        binding.bannerViewPager.adapter = bannerAdapter

        binding.bannerViewPager.registerOnPageChangeCallback(object :
            ViewPager2.OnPageChangeCallback() {
            override fun onPageSelected(position: Int) {
                currentPage = position
                initDots(position)
            }
        })
    }

    private fun startAutoScroll() {

        handler.removeCallbacksAndMessages(null)

        handler.postDelayed(object : Runnable {
            override fun run() {

                val count = bannerAdapter.itemCount
                if (count <= 1) return

                currentPage = (currentPage + 1) % count
                binding.bannerViewPager.setCurrentItem(currentPage, true)

                handler.postDelayed(this, 3000)
            }
        }, 3000)
    }

    private fun initDots(position: Int) {

        val count = bannerAdapter.itemCount

        if (count <= 1) {
            binding.layoutDots.visibility = View.GONE
            return
        }

        binding.layoutDots.visibility = View.VISIBLE
        binding.layoutDots.removeAllViews()

        repeat(count) {

            val dot = View(this)
            dot.setBackgroundResource(
                if (it == position) R.drawable.dot_active
                else R.drawable.dot_inactive
            )

            val params = LinearLayout.LayoutParams(16, 16)
            params.marginEnd = 8
            dot.layoutParams = params

            binding.layoutDots.addView(dot)
        }
    }

    // ---------------- CLICKS ----------------
    private fun attachClicks() {

        binding.swipeRefreshLayout.setOnRefreshListener {
            viewModel.getHome(pref.token, pref.userId)
        }

        binding.layoutBookings.setOnClickListener {
            openA<BookingsActivity>()
        }

        binding.layoutEarnings.setOnClickListener {

        }

        binding.layoutRoutes.setOnClickListener {
            openA<RoutesActivity>()
        }

        binding.layoutProfile.setOnClickListener {
            openA<UserProfileActivity>()
        }
    }

    // ---------------- BOOKING CLICK ----------------
    override fun onBookingClick(booking: BookingsResponseModel.Booking) {

        val intent = Intent(this, BookingDetailsActivity::class.java)
        intent.putExtra("booking", booking)
        startActivity(intent)
    }

    // ---------------- CLEANUP ----------------
    override fun onDestroy() {
        super.onDestroy()
        handler.removeCallbacksAndMessages(null)
    }
}