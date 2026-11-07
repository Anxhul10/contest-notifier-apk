package com.anxhul10.contest_notifier_apk.data.external.dto

data class ContestRes(
    val name: String,
    val durationSeconds: Int,
    val startTimeSeconds: Long,
    val relativeTimeSeconds: Long

)
data class CodeforcesResponse(
    val status: String,
    val result: List<ContestRes>
)