package com.anxhul10.contest_notifier_apk.utils

interface AlarmScheduler {
    fun schedule(item: AlarmItem)
    fun cancel(item: AlarmItem)
}