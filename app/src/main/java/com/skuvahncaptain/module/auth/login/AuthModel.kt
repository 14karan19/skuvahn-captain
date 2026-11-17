package com.skuvahncaptain.module.auth.login

data class SendOtpRequest(
    val phone: String,
    val roleType: String = "driver"
)

data class VerifyOtpRequest(
    val phone: String,
    val otp: String,
    val roleType: String = "driver"
)

data class ResendOtpRequest(
    val phone: String
)

data class SendOtpResponse(
    val userId: String,
    val phone: String,
    val isNewUser: Boolean
)

data class VerifyOtpResponse(
    val userId: String,
    val fullName: String,
    val accessToken: String,
    val isProfileComplete: Boolean
)

data class BaseResponse<T>(
    val success: Boolean,
    val statusCode: Int,
    val message: String?,
    val messages: List<String>?,
    val errors: Any?,
    val data: T?
)