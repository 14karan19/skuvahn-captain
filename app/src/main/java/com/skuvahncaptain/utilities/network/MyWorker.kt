package com.skuvahncaptain.utilities.network

import android.content.Context
import android.os.Handler
import android.os.Looper
import androidx.work.Worker
import androidx.work.WorkerParameters
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch

internal class MyWorker(val context: Context, params: WorkerParameters) : Worker(context, params) {

    private var job = Job()
    private var coroutineScope = CoroutineScope(Dispatchers.Default + job)

    override fun doWork(): Result {
        val handler = Handler(Looper.getMainLooper())
        handler.post {
            syncData(context)
        }
        return Result.success()
    }

    private fun syncData(context: Context) {
        coroutineScope.launch(Dispatchers.IO) {
//            val uploadManager = UploadManager(context)
//            if (CommonUtils.isConnectingToInternet(context)) {
//                uploadManager.uploadSyncFiles()
//            }
        }
    }

}
