package com.anxhul10.contest_notifier_apk.data.external.api

import com.anxhul10.contest_notifier_apk.data.external.dto.CodeforcesResponse
import retrofit2.http.GET

interface APICodeforces {
    @GET("api/contest.list")
    suspend fun getContests(): CodeforcesResponse
}