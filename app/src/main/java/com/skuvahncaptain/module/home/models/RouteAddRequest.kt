package com.skuvahncaptain.module.home.models

data class RouteAddRequest(
    val captainId: String,
    val schoolId: String,
    val routeStop: String,
    val address: String,
    val pincode: String,
    val landmark: String,
    val latitude: String?,
    val longitude: String,
)