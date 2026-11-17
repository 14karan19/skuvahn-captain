package com.skuvahncaptain.utilities.network

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import androidx.work.ExistingWorkPolicy
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.OutOfQuotaPolicy
import androidx.work.WorkManager

class AlarmReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context, intent: Intent?) {
        // Schedule the next alarm (manual repeat every 15 minutes)
        AlarmUtils.scheduleNextAlarm(context)

        // Start unique work
        val workRequest = OneTimeWorkRequestBuilder<MyWorker>()
            .setExpedited(OutOfQuotaPolicy.RUN_AS_NON_EXPEDITED_WORK_REQUEST)
            .build()

        WorkManager.getInstance(context).enqueueUniqueWork(
            "FifteenMinuteTask",
            ExistingWorkPolicy.KEEP,
            workRequest
        )

    }
}
