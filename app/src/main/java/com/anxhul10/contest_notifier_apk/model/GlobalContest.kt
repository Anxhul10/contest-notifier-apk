package com.anxhul10.contest_notifier_apk.model

data class GlobalContest(
    val name: String,
    val platform: String,
    val contestDuration: Int,
    val relativeTimeSeconds: Long,
    val startTimeSeconds: Long
)
