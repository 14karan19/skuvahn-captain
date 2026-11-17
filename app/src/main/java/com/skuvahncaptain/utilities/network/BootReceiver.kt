package com.skuvahncaptain.utilities.network

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.util.Log

class BootReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context, intent: Intent?) {

        Log.d("BootReceiver", "Alarm fired!")

        if (intent?.action == Intent.ACTION_BOOT_COMPLETED) {
            if (intent?.action == Intent.ACTION_BOOT_COMPLETED) {
                Log.d("BootReceiver", "Device rebooted - rescheduling 15-min alarm")
                AlarmUtils.scheduleNextAlarm(context)
            }

        }
    }
}
