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
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.launch
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.time.Instant

class RetrofitInstances {
    suspend fun runner(): List<GlobalContest> = coroutineScope{
        val codechef = async {codeChefInstance()}
        val codeforces = async {codeforcesInstance()}
        val leetcode = async {leetCodeInstance()}

        val results = awaitAll(codechef, codeforces, leetcode)

        results.flatten().sortedBy { it.startTimeSeconds };
    }
    suspend fun codeChefInstance(): List<GlobalContest> {
        val contests  = mutableListOf<GlobalContest>();

        val codechefAPI = Retrofit.Builder()
            .baseUrl("https://www.codechef.com/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(APICodechef::class.java)

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

        return contests;
    }
    suspend fun codeforcesInstance(): List<GlobalContest> {
        val contests = mutableListOf<GlobalContest>();

        val codeforcesAPI = Retrofit.Builder()
            .baseUrl("https://codeforces.com/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(APICodeforces::class.java)

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

        return contests;
    }

    suspend fun leetCodeInstance(): List<GlobalContest> {
        val contests = mutableListOf<GlobalContest>();
        val leetcodeAPI = Retrofit.Builder()
            .baseUrl("https://leetcode.com/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(LeetCodeAPI::class.java)

        try {
            val response = leetcodeAPI.getContests(
                LeetCodeRequest(
                    query = LeetCodeQueries.UPCOMING_CONTESTS
                )
            )
            response.data.contestV2UpcomingContests.forEach { contest ->
                val relativeTimeSeconds = Instant.now().epochSecond
                contests.add(
                    GlobalContest(
                        name = contest.title,
                        platform = "leetcode",
                        contestDuration = contest.duration.toInt(),
                        relativeTimeSeconds = Instant.now().epochSecond - contest.startTime,
                        startTimeSeconds = contest.startTime
                    )
                )
            }

        } catch (e: Exception) {
            Log.e("Retrofit Instance", "LeetCode API failed", e)
        }

        return contests;
    }
}
