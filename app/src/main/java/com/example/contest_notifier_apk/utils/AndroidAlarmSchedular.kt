package com.example.contest_notifier_apk.utils

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.util.Log
import java.time.ZoneId
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
    
        Log.d(
            "AlarmScheduler",
            "Scheduling alarm: ${item.message}, " +
                "time=${item.time}, millis=$alarmTimeMillis"
        )
    
        if (alarmTimeMillis <= System.currentTimeMillis()) {
            Log.w("AlarmScheduler", "Skipping alarm because its time has passed")
            return
        }
    
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.S &&
            !alarmManager.canScheduleExactAlarms()
        ) {
            Log.e(
                "AlarmScheduler",
                "Exact alarm access is not permitted"
            )
            return
        }
    
        alarmManager.setExactAndAllowWhileIdle(
            AlarmManager.RTC_WAKEUP,
            alarmTimeMillis,
            pendingIntent
        )
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
