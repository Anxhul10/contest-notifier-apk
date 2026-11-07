package com.anxhul10.contest_notifier_apk.data.external

import android.util.Log
import com.anxhul10.contest_notifier_apk.data.external.api.APICodechef
import com.anxhul10.contest_notifier_apk.data.external.api.APICodeforces
import com.anxhul10.contest_notifier_apk.data.external.api.LeetCodeAPI
import com.anxhul10.contest_notifier_apk.data.external.dto.LeetCodeQueries
import com.anxhul10.contest_notifier_apk.data.external.dto.LeetCodeRequest
import com.anxhul10.contest_notifier_apk.model.GlobalContest
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.time.Instant

class RetrofitInstances {
    val contests = mutableListOf<GlobalContest>();

    suspend fun CodeChefInstance() {
        val codechefAPI = Retrofit.Builder()
            .baseUrl("https://www.codechef.com/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(APICodechef::class.java)

        CoroutineScope(Dispatchers.IO).launch {
            try {
                val response = codechefAPI.getContests()
                if(response.status == "success") {
                    response.future_contests.forEach {
                        contest ->
                        val startTimeSeconds = Instant.parse(contest.contest_start_date_iso).epochSecond
                        val relativeTimeSeconds = Instant.now().epochSecond - startTimeSeconds
                        contests.add(
                            GlobalContest(
                                name = contest.contest_name,
                                platform = "codechef",
                                contestDuration = contest.contest_duration.toInt()*60,
                                relativeTimeSeconds = relativeTimeSeconds,
                                startTimeSeconds = startTimeSeconds
                            )
                        )
                    }
                }
            } catch (e: Exception) {
                Log.e("Retrofit Instance", "CodeChef API failed", e)
            }
        }
    }
    suspend fun CodeforcesInstance() {
        val codeforcesAPI = Retrofit.Builder()
            .baseUrl("https://codeforces.com/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(APICodeforces::class.java)

        CoroutineScope(Dispatchers.IO).launch {
            try {
                val response = codeforcesAPI.getContests()
                if(response.status =="OK") {
                    response.result.forEach {
                        contest ->
                        if(contest.relativeTimeSeconds < 0) {
                            contests.add(
                                GlobalContest(
                                    name = contest.name,
                                    platform = "codeforces",
                                    contestDuration = contest.durationSeconds,
                                    relativeTimeSeconds = contest.relativeTimeSeconds,
                                    startTimeSeconds = contest.startTimeSeconds
                                )
                            )
                        }
                    }
                }
            } catch(e: Exception) {
                Log.e("Retrofit Instance", "Codeforces API failed", e)
            }
        }
    }
}

