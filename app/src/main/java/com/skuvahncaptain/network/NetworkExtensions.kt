package com.skuvahncaptain.network

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Build
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.net.InetSocketAddress
import java.net.Socket

suspend fun Context.isWiFiConnectedWithInternet(): Boolean = withContext(Dispatchers.IO) {
    val connectivityManager = getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager?

    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
        val network = connectivityManager?.activeNetwork
        val capabilities = connectivityManager?.getNetworkCapabilities(network)

        // Check if it's a Wi-Fi or mobile data network with internet access
        return@withContext (capabilities?.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) == true ||
                capabilities?.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) == true) &&
                isInternetReachable()
    } else {
        // For older versions, check if it's a Wi-Fi or mobile data network and perform the internet reachability check
        val activeNetworkInfo = connectivityManager?.activeNetworkInfo
        return@withContext (activeNetworkInfo?.type == ConnectivityManager.TYPE_WIFI ||
                activeNetworkInfo?.type == ConnectivityManager.TYPE_MOBILE) &&
                isInternetReachable()
    }
}


private fun Context.isInternetReachable(): Boolean {
    return try {
        val timeoutMs = 1500
        val socket = Socket()
        val socketAddress = InetSocketAddress("www.google.com", 80)

        socket.connect(socketAddress, timeoutMs)
        socket.close()
        true
    } catch (e: Exception) {
        false
    }
}
