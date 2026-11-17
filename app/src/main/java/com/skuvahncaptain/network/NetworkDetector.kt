package com.skuvahncaptain.network

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch

@AndroidEntryPoint
class NetworkDetector : BroadcastReceiver() {

    private val eventChannel = Channel<Boolean>()
    val searched = eventChannel.receiveAsFlow()

    // var searched = MutableLiveData<Boolean>()
    override fun onReceive(p0: Context?, p1: Intent?) {
        GlobalScope.launch {
            p0?.isWiFiConnectedWithInternet()?.let { eventChannel.send(it) }
        }
    }
}