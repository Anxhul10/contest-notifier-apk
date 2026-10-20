
package com.example.contest_notifier_apk.utils

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.util.Log

class AlarmReceiver : BroadcastReceiver() {

    override fun onReceive(context: Context, intent: Intent?) {
        val message = intent?.getStringExtra("EXTRA_MESSAGE")
            ?: "Your contest is starting soon!"

        Log.d("AlarmReceiver", "Alarm triggered: $message")

        NotificationHelper.showNotification(context, message)
    }
}
