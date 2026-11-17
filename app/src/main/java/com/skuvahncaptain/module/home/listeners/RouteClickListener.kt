package com.skuvahncaptain.module.home.listeners

import com.skuvahncaptain.module.home.models.RouteResponseModel

interface RouteClickListener {
    fun onRouteClick(address: RouteResponseModel.Route)
}
