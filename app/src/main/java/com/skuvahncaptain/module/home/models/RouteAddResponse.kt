package com.skuvahncaptain.module.home.models

data class RouteAddResponse(
    val success: Boolean,
    val statusCode: Int,
    val message: String?,
    val messages: List<String>?,
    val errors: Map<String, List<String>>?,
    val data: String // addressId
)