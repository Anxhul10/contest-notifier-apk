package com.anxhul10.contest_notifier_apk.data.external.dto

data class LeetCodeResponse(
    val data: LeetCodeData
)

data class LeetCodeData(
    val contestV2UpcomingContests: List<LeetCodeContest>
)

data class LeetCodeContest(
    val title: String,
    val startTime: Long,
    val duration: Long
)