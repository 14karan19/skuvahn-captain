package com.skuvahncaptain.base.model

sealed class NetworkResultModel<T>(val data: T? = null, val message: String? = null, val type: String? = null) {

    class Success<T>(data: T,type:String?) : NetworkResultModel<T>(data, type = type)
    class Error<T>(message: String?, data: T? = null) : NetworkResultModel<T>(data, message)
    class Loading<T> : NetworkResultModel<T>()

}