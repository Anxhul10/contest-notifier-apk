package com.anxhul10.contest_notifier_apk.data.external

import android.util.Log
import com.anxhul10.contest_notifier_apk.data.external.api.APICodechef
import com.anxhul10.contest_notifier_apk.data.external.api.APICodeforces
import com.anxhul10.contest_notifier_apk.data.external.api.LeetCodeAPI
import com.anxhul10.contest_notifier_apk.data.external.dto.LeetCodeQueries
import com.anxhul10.contest_notifier_apk.data.external.dto.LeetCodeRequest
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

fun runner() {

    val codechefAPI = Retrofit.Builder()
        .baseUrl("https://www.codechef.com/")
        .addConverterFactory(GsonConverterFactory.create())
        .build()
        .create(APICodechef::class.java)

    val codeforcesAPI = Retrofit.Builder()
        .baseUrl("https://codeforces.com/")
        .addConverterFactory(GsonConverterFactory.create())
        .build()
        .create(APICodeforces::class.java)

    val leetcodeAPI = Retrofit.Builder()
        .baseUrl("https://leetcode.com/")
        .addConverterFactory(GsonConverterFactory.create())
        .build()
        .create(LeetCodeAPI::class.java)

    CoroutineScope(Dispatchers.IO).launch {

        try {

            val response = leetcodeAPI.getContests(
                LeetCodeRequest(
                    query = LeetCodeQueries.UPCOMING_CONTESTS
                )
            )

            response.data.contestV2UpcomingContests.forEach { contest ->

                Log.d("LEETCODE", "Title: ${contest.title}")
                Log.d("LEETCODE", "Start: ${contest.startTime}")
                Log.d("LEETCODE", "Duration: ${contest.duration}")
            }

        } catch (e: Exception) {
            Log.e("LEETCODE", "API Error", e)
        }
    }
}