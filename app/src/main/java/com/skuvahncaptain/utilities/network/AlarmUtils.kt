package com.skuvahncaptain.utilities.network

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import android.provider.Settings
import android.util.Log
import java.util.Date

object AlarmUtils {
    private const val ALARM_INTERVAL_MINS = 5
    private const val PREF_NAME = "AlarmPrefs"
    private const val KEY_NEXT_ALARM_TIME = "next_alarm_time"

    fun scheduleNextAlarm(context: Context) {
        val prefs = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
        val lastScheduledTime = prefs.getLong(KEY_NEXT_ALARM_TIME, 0L)
        val currentTime = System.currentTimeMillis()

        // Check if previous alarm is still valid
        if (lastScheduledTime > currentTime) {
            Log.d("AlarmScheduler", "Alarm already scheduled for future: ${Date(lastScheduledTime)}")
            return  // Don't schedule another
        }

        // Calculate next trigger time
        val triggerTimeMillis = currentTime + ALARM_INTERVAL_MINS * 60 * 1000

        val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
        val intent = Intent(context, AlarmReceiver::class.java)

        val pendingIntent = PendingIntent.getBroadcast(
            context,
            1001,
            intent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )

        // Handle Android 12+
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S &&
            !alarmManager.canScheduleExactAlarms()
        ) {
            val requestIntent = Intent(Settings.ACTION_REQUEST_SCHEDULE_EXACT_ALARM)
            requestIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            context.startActivity(requestIntent)
            return
        }

        // Schedule exact alarm
        alarmManager.setExactAndAllowWhileIdle(
            AlarmManager.RTC_WAKEUP,
            triggerTimeMillis,
            pendingIntent
        )

        // Save alarm time
        prefs.edit().putLong(KEY_NEXT_ALARM_TIME, triggerTimeMillis).apply()

        Log.d("AlarmScheduler", "New alarm scheduled for: ${Date(triggerTimeMillis)}")
    }

    fun scheduleExactAlarm(context: Context, triggerTimeMillis: Long) {
        val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
        val intent = Intent(context, AlarmReceiver::class.java)

        val pendingIntent = PendingIntent.getBroadcast(
            context,
            1001,
            intent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )

        // Save trigger time for reboot persistence
        context.getSharedPreferences("AlarmPrefs", Context.MODE_PRIVATE).edit().apply {
            putLong("trigger_time", triggerTimeMillis)
            apply()
        }

        // Android 12+ requires permission
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            if (!alarmManager.canScheduleExactAlarms()) {
                val requestIntent = Intent(Settings.ACTION_REQUEST_SCHEDULE_EXACT_ALARM)
                requestIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                context.startActivity(requestIntent)
                return
            }
        }

        alarmManager.setExactAndAllowWhileIdle(
            AlarmManager.RTC_WAKEUP,
            triggerTimeMillis,
            pendingIntent
        )

        Log.d("AlarmScheduler", "Alarm scheduled for: ${Date(triggerTimeMillis)}")
    }

    fun scheduleRepeatingServiceOnce(context: Context) {
        val prefs = context.getSharedPreferences("AppPrefs", Context.MODE_PRIVATE)
        if (!prefs.getBoolean("alarmScheduled", false)) {

            val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
            val intent = Intent(context, AlarmReceiver::class.java)
            val pendingIntent = PendingIntent.getBroadcast(
                context, 0, intent,
                PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
            )

            val intervalMillis = 15 * 60 * 1000L
            val triggerAtMillis = System.currentTimeMillis() + intervalMillis

            alarmManager.setRepeating(
                AlarmManager.RTC_WAKEUP,
                triggerAtMillis,
                intervalMillis,
                pendingIntent
            )

            prefs.edit().putBoolean("alarmScheduled", true).apply()
            Log.d("AlarmUtils", "Alarm scheduled successfully.")
        } else {
            Log.d("AlarmUtils", "Alarm already scheduled.")
        }
    }
}
