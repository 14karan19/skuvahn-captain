package com.skuvahncaptain.module.home.viewmodels

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.skuvahncaptain.base.api.ApiInterface
import com.skuvahncaptain.module.home.states.HomeState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val api: ApiInterface
) : ViewModel() {

    val state = MutableLiveData<HomeState>()

    fun getHome(token:String, userId: String) = viewModelScope.launch {
        state.value = HomeState.Loading
        try {
            val res = api.getHome(token, userId)

            if (res.isSuccessful && res.body()?.success == true) {
                state.value = HomeState.Success(res.body()!!.data!!)
            } else {
                state.value = HomeState.Error(res.body()?.message ?: "Something went wrong")
            }

        } catch (e: Exception) {
            state.value = HomeState.Error(e.message ?: "Error")
        }
    }
}