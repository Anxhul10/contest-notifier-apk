package com.anxhul10.contest_notifier_apk.data.external.api

import com.anxhul10.contest_notifier_apk.data.external.dto.LeetCodeRequest
import com.anxhul10.contest_notifier_apk.data.external.dto.LeetCodeResponse
import retrofit2.http.Body
import retrofit2.http.POST

interface LeetCodeAPI {

    @POST("graphql/")
    suspend fun getContests(
        @Body request: LeetCodeRequest
    ): LeetCodeResponse
}