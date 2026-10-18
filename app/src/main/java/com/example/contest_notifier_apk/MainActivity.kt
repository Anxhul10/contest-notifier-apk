package com.example.contest_notifier_apk
import androidx.compose.runtime.mutableStateListOf
import android.R
import android.content.res.Configuration
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Column
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.lifecycleScope
import com.example.contest_notifier_apk.ui.components.CardWrapper
import com.example.contest_notifier_apk.ui.theme.ContestnotifierapkTheme
import com.example.contest_notifier_apk.ui.components.TopBar
import kotlinx.coroutines.launch
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.create
import retrofit2.http.GET
import retrofit2.http.Path

data class ContestRes(
    val name: String,
    val durationSeconds: Int,
    val startTimeSeconds: Long,
    val relativeTimeSeconds: Long

)
data class Contests(
    val status: String,
    val result: List<ContestRes>
)
interface APIService {
    @GET("api/{platform}")

    suspend fun getData(
        @Path("platform") platform: String
    ): Contests
}
data class ContestData(val name: String, val platform: String, val contestTime: Int, val relativeTimeSeconds: Long)
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val api: APIService = Retrofit.Builder()
            .baseUrl("https://contest-notifier-apk.onrender.com/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(APIService::class.java)
        val contestStorage = mutableStateListOf<ContestData>();
        val tmpContestStorage = mutableListOf<ContestData>();
        setContent {
            ContestnotifierapkTheme {
                TopBar(contestStorage)
            }
        }

        lifecycleScope.launch {
            val platforms = listOf(
                "leetcode",
                "codeforces",
                "codechef"
            )

            for(platform in platforms) {
                try {
                    val res = api.getData(platform);

                    res.result.forEach {
                        contest ->

                        val tmpData = ContestData(
                            name = contest.name,
                            platform = platform,
                            contestTime = contest.durationSeconds,
                            relativeTimeSeconds = contest.relativeTimeSeconds
                        )
                        tmpContestStorage.add(tmpData);
                    }
                } catch (e: Exception) {
                    Log.e("API", "Error: ${e.message}");
                }
            }
            val sortedOrder = tmpContestStorage
                .filter {
                    it.relativeTimeSeconds <= 0
                }
                .sortedByDescending {
                    it.relativeTimeSeconds
                }

            contestStorage.clear();
            contestStorage.addAll(sortedOrder);

        }
    }
}


@Composable
fun ContestList(modifier: Modifier = Modifier, names: List<ContestData> = emptyList()) {
    Surface (
        modifier = modifier,
        color = MaterialTheme.colorScheme.background
    ) {
        Column () {
            CardWrapper(modifier, names)
        }
    }
}