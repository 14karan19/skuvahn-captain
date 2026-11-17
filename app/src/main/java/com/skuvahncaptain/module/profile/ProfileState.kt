package com.skuvahncaptain.module.profile

sealed class ProfileState {

    object Loading : ProfileState()

    data class Success(
        val captainId: String,
        val vehicleId: String
    ) : ProfileState()

    object DeleteSuccess : ProfileState()

    data class Error(val message: String) : ProfileState()
}