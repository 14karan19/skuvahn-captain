package com.skuvahncaptain.module.home.models

data class RoutesResponseModel(
    val success: Boolean,
    val statusCode: Int,
    val message: String?,
    val messages: List<String>?,
    val errors: Map<String, List<String>>?,
    val data: List<Route>?
) {
    data class Route(
        val id: String,
        val captainId: String,
        val routeStop: String,
        val landmark: String?,
        val latitude: String?,
        val longitude: String?,
        val isActive: Boolean
    )
}