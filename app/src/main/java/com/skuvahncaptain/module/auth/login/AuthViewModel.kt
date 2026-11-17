package com.skuvahncaptain.module.auth.login

import android.app.Activity
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.skuvahncaptain.base.api.ApiInterface
import com.skuvahncaptain.utilities.AppPref
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AuthViewModel @Inject constructor(
    private val api: ApiInterface
) : ViewModel() {

    val state = MutableLiveData<AuthState>()

    // ---------------- SEND OTP ----------------
    fun sendOtp(phone: String) {
        viewModelScope.launch {

            state.postValue(AuthState.Loading)

            try {
                val response = api.sendOtp(
                    SendOtpRequest(phone = phone)
                )

                if (response.success && response.data != null) {

                    state.postValue(
                        AuthState.OtpSent(
                            userId = response.data.userId,
                            phone = response.data.phone,
                            isNewUser = response.data.isNewUser
                        )
                    )

                } else {
                    state.postValue(
                        AuthState.Error(response.message ?: "Failed to send OTP")
                    )
                }

            } catch (e: Exception) {
                state.postValue(
                    AuthState.Error(e.message ?: "Something went wrong")
                )
            }
        }
    }

    // ---------------- RESEND OTP ----------------
    fun resendOtp(phone: String) {
        viewModelScope.launch {

            state.postValue(AuthState.Loading)

            try {
                val response = api.resendOtp(
                    ResendOtpRequest(phone = phone)
                )

                if (response.success) {
                    state.postValue(AuthState.CodeSent)
                } else {
                    state.postValue(
                        AuthState.Error(response.message ?: "Failed to resend OTP")
                    )
                }

            } catch (e: Exception) {
                state.postValue(
                    AuthState.Error(e.message ?: "Something went wrong")
                )
            }
        }
    }

    // ---------------- VERIFY OTP ----------------
    fun verifyOtp(phone: String, otp: String) {
        viewModelScope.launch {

            state.postValue(AuthState.Loading)

            try {
                val response = api.verifyOtp(
                    VerifyOtpRequest(phone = phone, otp = otp)
                )

                if (response.success && response.data != null) {

                    state.postValue(
                        AuthState.Verified(
                            accessToken = response.data.accessToken,
                            isProfileComplete = response.data.isProfileComplete
                        )
                    )

                } else {
                    state.postValue(
                        AuthState.Error(response.message ?: "Invalid OTP")
                    )
                }

            } catch (e: Exception) {
                state.postValue(
                    AuthState.Error(e.message ?: "Verification failed")
                )
            }
        }
    }
}