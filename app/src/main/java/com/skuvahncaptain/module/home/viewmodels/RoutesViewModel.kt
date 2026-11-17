package com.skuvahncaptain.module.home.viewmodels

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.skuvahncaptain.base.api.ApiInterface
import com.skuvahncaptain.module.home.models.RouteAddRequest
import com.skuvahncaptain.module.home.states.RouteState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class RoutesViewModel @Inject constructor(
    private val api: ApiInterface
) : ViewModel() {

    val state = MutableLiveData<RouteState>()

    fun myRoutes(token: String, userId: String) = viewModelScope.launch {

        state.value = RouteState.Loading

        try {
            val response = api.myRoutes(token, userId)

            if (response.isSuccessful) {
                val body = response.body()

                if (body?.success == true) {
                    state.value = RouteState.Success(body.data ?: emptyList())
                } else {
                    state.value = RouteState.Error(body?.message ?: "Failed")
                }
            } else {
                state.value = RouteState.Error("Error ${response.code()}")
            }

        } catch (e: Exception) {
            state.value = RouteState.Error(e.message ?: "Error")
        }
    }

    fun addRoute(token: String, request: RouteAddRequest) = viewModelScope.launch {

        state.value = RouteState.Loading

        try {
            val response = api.addRoute(token,request)

            if (response.isSuccessful) {
                val body = response.body()

                if (body?.success == true) {
                    state.value = RouteState.AddSuccess(
                        body.message ?: "Route added successfully"
                    )
                } else {
                    state.value = RouteState.Error(
                        body?.message ?: "Failed to add route"
                    )
                }

            } else {
                state.value = RouteState.Error("Server error: ${response.code()}")
            }

        } catch (e: Exception) {
            state.value = RouteState.Error(e.message ?: "Something went wrong")
        }
    }

    fun loadBookingDropdowns(token: String, userId: String) = viewModelScope.launch {

        state.value = RouteState.Loading

        try {
            val schoolRes = api.getSchools(token)

            state.value = RouteState.DropdownData(
                schools = schoolRes.body()?.data ?: ArrayList()
            )

        } catch (e: Exception) {
            state.value = RouteState.Error(e.message ?: "Failed to load data")
        }
    }

    fun deleteRoute(token:String, routeId: String, userId: String) = viewModelScope.launch {

        state.value = RouteState.Loading

        try {
            val response = api.deleteRoute(token, routeId, userId)

            if (response.isSuccessful) {
                val body = response.body()

                if (body?.success == true && body.data == true) {
                    state.value = RouteState.DeleteSuccess(
                        body.message ?: "Deleted"
                    )
                } else {
                    state.value = RouteState.Error(body?.message ?: "Delete failed")
                }

            } else {
                state.value = RouteState.Error("Error ${response.code()}")
            }

        } catch (e: Exception) {
            state.value = RouteState.Error(e.message ?: "Error")
        }
    }
}