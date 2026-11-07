package com.anxhul10.contest_notifier_apk.data.external.dto

data class CodechefRes(
    val status: String,
    val message: String,
    val present_contests: List<CodechefContest>,
    val future_contests: List<CodechefContest>
)

data class CodechefContest(
    val contest_id: String,
    val contest_code: String,
    val contest_name: String,
    val contest_start_date_iso: String,
    val contest_end_date_iso: String,
    val contest_duration: String,
    val distinct_users: Int
)