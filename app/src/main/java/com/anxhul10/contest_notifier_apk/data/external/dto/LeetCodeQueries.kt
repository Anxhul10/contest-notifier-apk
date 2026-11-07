package com.anxhul10.contest_notifier_apk.data.external.dto

object LeetCodeQueries {

    val UPCOMING_CONTESTS = """
        {
            contestV2UpcomingContests {
                title
                startTime
                duration
            }
        }
    """.trimIndent()
}