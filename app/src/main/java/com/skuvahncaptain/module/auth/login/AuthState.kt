package com.skuvahncaptain.module.auth.login

sealed class AuthState {
    object Loading : AuthState()

    // send OTP success
    data class OtpSent(
        val userId: String,
        val phone: String,
        val isNewUser: Boolean
    ) : AuthState()

    // resend OTP
    object CodeSent : AuthState()

    // verify OTP
    data class Verified(
        val accessToken: String,
        val isProfileComplete: Boolean
    ) : AuthState()

    data class Error(val message: String) : AuthState()
}