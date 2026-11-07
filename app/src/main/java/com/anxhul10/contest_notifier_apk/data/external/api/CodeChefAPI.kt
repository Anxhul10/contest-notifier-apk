package com.anxhul10.contest_notifier_apk.data.external.api

import com.anxhul10.contest_notifier_apk.data.external.dto.CodechefRes
import retrofit2.http.GET
import retrofit2.http.Query

interface APICodechef {
    @GET("api/list/contests/all")
    suspend fun getContests(
        @Query("sort_by") sortBy: String = "START",
        @Query("sorting_order") sortingOrder: String = "asc",
        @Query("offset") offset: Int = 0,
        @Query("mode") mode: String = "all"
    ): CodechefRes
}