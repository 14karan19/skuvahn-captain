package com.skuvahncaptain.module.home.listeners

import com.skuvahncaptain.module.home.models.RouteResponseModel

interface RouteDeleteClickListener {
    fun onDeleteRouteClick(address: RouteResponseModel.Route)
}
