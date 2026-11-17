package com.skuvahncaptain.module.home.states

import com.skuvahncaptain.module.home.models.HomeResponseModel

sealed class HomeState {
    object Idle : HomeState()
    object Loading : HomeState()
    data class Success(val data: HomeResponseModel.Data) : HomeState()
    data class Error(val message: String) : HomeState()
}