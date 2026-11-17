package com.skuvahncaptain.module.home.models

data class HomeResponseModel(
    val success: Boolean,
    val statusCode: Int,
    val message: String?,
    val data: Data?
) {

    data class Data(
        val user: User?,
        val banners: List<Banner>?,
        val activeBookings: ArrayList<BookingsResponseModel.Booking>?
    )

    data class User(
        val id: String,
        val fullName: String,
        val phone: String,
        val email: String?,
        val imageUrl: String?
    )

    data class Banner(
        val id: String,
        val bannerUrl: String
    )
}