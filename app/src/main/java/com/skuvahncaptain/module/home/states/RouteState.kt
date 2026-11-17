package com.skuvahncaptain.module.home.states

import com.skuvahncaptain.module.home.models.RouteResponseModel
import com.skuvahncaptain.module.home.models.SchoolResponseModel

sealed class RouteState {

    object Loading : RouteState()

    data class Success(
        val list: List<RouteResponseModel.Route> = emptyList()
    ) : RouteState()

    data class AddSuccess(val message: String) : RouteState()

    data class DeleteSuccess(val message: String) : RouteState()

    data class Error(val message: String) : RouteState()

    data class DropdownData(
        val schools: ArrayList<SchoolResponseModel.School>
    ) : RouteState()
}