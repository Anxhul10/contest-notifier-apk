package com.example.contest_notifier_apk
import androidx.compose.runtime.mutableStateListOf
import android.R
import android.content.res.Configuration
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.lifecycleScope
import com.example.contest_notifier_apk.ui.components.CardWrapper
import com.example.contest_notifier_apk.ui.theme.ContestnotifierapkTheme
import com.example.contest_notifier_apk.ui.components.TopBar
import com.google.gson.Gson
import kotlinx.coroutines.launch
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.create
import retrofit2.http.GET
import retrofit2.http.Path
import android.content.Context
import com.google.gson.reflect.TypeToken
import androidx.core.content.edit

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
data class Health(
    val status: String
)
interface APIService {
    @GET("api/{platform}")
    suspend fun getData(
        @Path("platform") platform: String
    ): Contests

    @GET("health")
    suspend fun checkHealth(): Health
}
data class ContestData(val name: String, val platform: String, val contestTime: Int, val relativeTimeSeconds: Long, val startTimeSeconds: Long)
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

            var isReady = false;

            repeat(10) {
                attempt ->
                try {
                    val health = api.checkHealth();
                    if(health.status.equals("ok", ignoreCase = true)) {
                        isReady = true;
                        Log.d("API", "Health: ${health.status}");
                        return@repeat;
                    } else {
                        Log.d("API", "Health check returned: ${health.status}");
                    }
                } catch(e: Exception) {
                    Log.e("API", "Health attempt ${attempt+1} : ${e.message}");
                }
                if(!isReady && attempt < 9) {
                    kotlinx.coroutines.delay(3000);
                }
            }
            if (!isReady) {
                Log.e("API", "Server unavailable after 10 attempts")
                return@launch
            }

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
                            relativeTimeSeconds = contest.relativeTimeSeconds,
                            startTimeSeconds = System.currentTimeMillis()/1000-contest.relativeTimeSeconds
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
            saveContests(this@MainActivity, contestStorage);
            contestStorage.forEach { contest ->
                Log.d(
                    "contestNotifier",
                    "Name: ${contest.name}" +
                            "platform: ${contest.platform}" +
                            "contest time: ${contest.contestTime}"+
                            "relative time: ${contest.relativeTimeSeconds}"
                )
            }
        }
    }
}

fun saveContests(context: Context, contests: List<ContestData>) {
    val json = Gson().toJson(contests);

    context.getSharedPreferences("contest_cache", Context.MODE_PRIVATE)
        .edit {
            putString("contests", json)
        }
}

fun loadContests(context: Context): List<ContestData> {
    val prefs = context.getSharedPreferences(
        "contest_cache",
        Context.MODE_PRIVATE
    )

    val json = prefs.getString("contests", null) ?: return emptyList();

    val type = object: TypeToken<List<ContestData>>() {}.type

    return Gson().fromJson(json, type);
}
@Composable
fun ContestList(modifier: Modifier = Modifier, names: List<ContestData> = emptyList()) {
    Surface (
        modifier = modifier,
        color = MaterialTheme.colorScheme.background
    ) {
        Column (
            modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            if(names.isEmpty()) {
                CircularProgressIndicator();
            } else {
                CardWrapper(modifier, names)
            }
        }
    }
}