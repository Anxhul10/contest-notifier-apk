package com.example.contest_notifier_apk.utils

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.util.Log
import java.time.ZoneId
import android.os.Build
class AndroidAlarmScheduler(
    private val context: Context
): AlarmScheduler {

    private val alarmManager = context.getSystemService(AlarmManager::class.java)

    override fun schedule(item: AlarmItem) {
        val intent = Intent(context, AlarmReceiver::class.java).apply {
            putExtra("EXTRA_MESSAGE", item.message)
        }
        val pendingIntent = PendingIntent.getBroadcast(
            context,
            item.hashCode(),
            intent,
            PendingIntent.FLAG_UPDATE_CURRENT or
                    PendingIntent.FLAG_IMMUTABLE
        )
        val alarmTimeMillis = item.time
            .atZone(ZoneId.systemDefault())
            .toInstant()
            .toEpochMilli()
        Log.d("AlarmReceiver", "Scheduling alarm for: ${item.time}")
//        alarmManager.set(
//            AlarmManager.RTC_WAKEUP,
//            alarmTimeMillis,
//            pendingIntent
//        )
        if(!alarmManager.canScheduleExactAlarms()) {
            Log.w("Alarm Scheduler", "Exact alarm permission not granted");
            return;
        }
        try {
            alarmManager.setExactAndAllowWhileIdle(
                AlarmManager.RTC_WAKEUP,
                alarmTimeMillis,
                pendingIntent
            )
        } catch(e: SecurityException) {
            Log.e("Alarm Scheduler", "Security exception: ${e.message}");
        }
    }

    override fun cancel(item: AlarmItem) {
        alarmManager.cancel(
            PendingIntent.getBroadcast(
                context,
                item.hashCode(),
                Intent(context, AlarmReceiver::class.java),
                PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
            )
        )
    }
}