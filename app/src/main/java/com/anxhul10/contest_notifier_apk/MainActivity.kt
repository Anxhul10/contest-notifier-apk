package com.anxhul10.contest_notifier_apk
import androidx.compose.runtime.mutableStateListOf
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.lifecycle.lifecycleScope
import com.anxhul10.contest_notifier_apk.ui.components.CardWrapper
import com.anxhul10.contest_notifier_apk.ui.theme.ContestnotifierapkTheme
import com.anxhul10.contest_notifier_apk.ui.components.TopBar
import com.google.gson.Gson
import kotlinx.coroutines.launch
import retrofit2.Retrofit
import java.time.LocalDateTime
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Path
import android.content.Context
import com.google.gson.reflect.TypeToken
import androidx.core.content.edit
import com.anxhul10.contest_notifier_apk.utils.AlarmItem
import com.anxhul10.contest_notifier_apk.utils.AndroidAlarmScheduler
import android.Manifest
import android.R
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import androidx.compose.foundation.clickable
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.room3.Entity
import androidx.room3.PrimaryKey
import androidx.room3.Dao
import androidx.room3.Query
import androidx.room3.Insert
import androidx.room3.Delete
import androidx.room3.Database
import androidx.room3.RoomDatabase
import androidx.room3.Room
import androidx.sqlite.driver.AndroidSQLiteDriver
import com.anxhul10.contest_notifier_apk.data.external.RetrofitInstances
import com.anxhul10.contest_notifier_apk.model.GlobalContest
import com.anxhul10.contest_notifier_apk.utils.GithubApiService
import com.anxhul10.contest_notifier_apk.utils.bodyFontFamily
import com.anxhul10.contest_notifier_apk.utils.headingFontFamily
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.withContext
import retrofit2.create

@Dao
interface ContestDao {
    @Query("SELECT * FROM contests")
    suspend fun getAllContests(): List<ContestEntity>

    @Query("SELECT * FROM contests WHERE id IN (:contestIds)")
    suspend fun loadAllContestByIds(contestIds: IntArray): List<ContestEntity>

    @Query("DELETE FROM contests")
    suspend fun deleteAllContests()

    @Insert
    suspend fun insertAll(contests: List<ContestEntity>)

    @Delete
    suspend fun delete(contest: ContestEntity)
}
// TODO: change durationSeconds to contestDuration for consistency
@Entity(tableName = "contests")
data class ContestEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val name: String,
    val durationSeconds: Int,
    val startTimeSeconds: Long,
    val platform: String,
    val relativeTimeSeconds: Long
)
@Database(entities = [ContestEntity::class], version = 1)
abstract class AppDatabase : RoomDatabase() {
    abstract fun contestDao(): ContestDao
}

data class ContestData(val name: String, val platform: String, val contestDuration: Int, val relativeTimeSeconds: Long, val startTimeSeconds: Long)
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU &&
            checkSelfPermission(Manifest.permission.POST_NOTIFICATIONS)
            != PackageManager.PERMISSION_GRANTED
        ) {
            requestPermissions(
                arrayOf(Manifest.permission.POST_NOTIFICATIONS),
                1001
            )
        }

        val schedular = AndroidAlarmScheduler(this)
        var alarmItem: AlarmItem? = null;

        alarmItem?.let(schedular::schedule)

        val contestRoomState = mutableStateListOf<GlobalContest>()
        val contestFetchState = mutableStateListOf<GlobalContest>();

        val db =
            Room.databaseBuilder<AppDatabase>(applicationContext, "database-name")
                .setDriver(AndroidSQLiteDriver())
                .build()
        val roomtInstance = db.contestDao();
        val updateAvailable = mutableStateOf(false);

        val packageInfo = packageManager.getPackageInfo(packageName, 0)
        val githubRetrofit = Retrofit.Builder()
            .baseUrl("https://api.github.com/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        val githubApi = githubRetrofit.create(GithubApiService::class.java)

        setContent {
            ContestnotifierapkTheme {
                TopBar(contestRoomState, contestFetchState.size == 0, updateAvailable)
            }
        }


        lifecycleScope.launch {
            try {
                val fetchJob = async {
                    RetrofitInstances().runner()
                }

                val storedContests: List<ContestEntity> = roomtInstance.getAllContests()

                if(storedContests.isEmpty()) {
                    val fetchContests = fetchJob.await()
                    contestFetchState.clear()
                    contestFetchState.addAll(fetchContests)
                    roomtInstance.deleteAllContests()
                    contestRoomState.addAll(contestFetchState)
                    roomtInstance.insertAll(
                        contestFetchState.map { contest ->
                            ContestEntity(
                                name = contest.name,
                                platform = contest.platform,
                                durationSeconds = contest.contestDuration,
                                relativeTimeSeconds = contest.relativeTimeSeconds,
                                startTimeSeconds = contest.startTimeSeconds
                            )
                        }
                    )
                } else {
                    contestRoomState.clear()
                    contestRoomState.addAll(
                        storedContests.map{ contest->
                            GlobalContest(
                                name = contest.name,
                                platform = contest.platform,
                                contestDuration = contest.durationSeconds,
                                relativeTimeSeconds = contest.relativeTimeSeconds,
                                startTimeSeconds = contest.startTimeSeconds
                            )

                        }
                    )
                    val fetchContests = fetchJob.await()
                    contestFetchState.clear()
                    contestFetchState.addAll(fetchContests)
                    if(!contestFetchState.isEmpty()) {
                        roomtInstance.deleteAllContests()
                        contestRoomState.clear()
                        contestRoomState.addAll(fetchContests)
                        roomtInstance.insertAll(
                            contestFetchState.map{
                                contest->
                                ContestEntity(
                                    name = contest.name,
                                    platform = contest.platform,
                                    durationSeconds = contest.contestDuration,
                                    relativeTimeSeconds = contest.relativeTimeSeconds,
                                    startTimeSeconds = contest.startTimeSeconds
                                )
                            }
                        )
                    }
                }
            } catch (e: Exception) {
                Log.e("Retrofit instance", "Failed to create retrofit instance", e)
            }

            // update banner
            try {
                val release = githubApi.getLatestRelease()
                val latestVersion = release.tag_name.removePrefix("v")

                updateAvailable.value = (!(latestVersion == packageInfo.versionName))

            } catch (e: Exception) {
                Log.e("UpdateCheck", "Failed to check update", e)
            }


            // notifications scheduler
            val scheduler = AndroidAlarmScheduler(this@MainActivity)

            contestFetchState.forEach { contest ->
                val alarmItem = AlarmItem(
                    id = "${contest.platform}_${contest.name}_${contest.startTimeSeconds}_10m",
                    time = LocalDateTime.now()
                        .plusSeconds(
                            contest.startTimeSeconds -
                                    System.currentTimeMillis() / 1000 - 600
                        ),
                    message = "${contest.name} starts in 10 minutes!"
                )
                Log.d("alarm debug", "trigger time ${alarmItem.time}")
                Log.d("alarm debug", "current time ${LocalDateTime.now()}")
                val alarmItem2 = AlarmItem(
                    id = "${contest.platform}_${contest.name}_${contest.startTimeSeconds}_1hr",
                    time = LocalDateTime.now()
                        .plusSeconds(
                            contest.startTimeSeconds -
                                    System.currentTimeMillis() / 1000 - 3600
                        ),
                    message = "${contest.name} starts in 1 hour!"
                )
                if (alarmItem.time.isAfter(LocalDateTime.now())) {
                    scheduler.schedule(alarmItem)
                }
                if(alarmItem2.time.isAfter(LocalDateTime.now())) {
                    scheduler.schedule(alarmItem2);
                }
            }
            contestFetchState.forEach { contest ->
                Log.d(
                    "contestNotifier",
                    "Name: ${contest.name}" +
                            "platform: ${contest.platform}" +
                            "contest time: ${contest.contestDuration}"+
                            "relative time: ${contest.relativeTimeSeconds}"
                )
            }
        }
    }
}

//fun saveContests(context: Context, contests: List<ContestData>) {
//    val json = Gson().toJson(contests);
//
//    context.getSharedPreferences("contest_cache", Context.MODE_PRIVATE)
//        .edit {
//            putString("contests", json)
//        }
//}

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
fun ContestList(modifier: Modifier = Modifier, names: List<GlobalContest> = emptyList(), isLoading: Boolean, updateAvailable: MutableState<Boolean>) {
    val context = LocalContext.current
    Surface (
        modifier = modifier,
        color = MaterialTheme.colorScheme.background
    ) {
        Column (
            modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            if (updateAvailable.value) {
                Surface(
                    color = if (isSystemInDarkTheme()) Color(0xFF203B3B) else Color(0xFFDCEFEA),
                    modifier = Modifier
                        .padding(vertical = 1.dp, horizontal = 6.dp)
                        .clickable{
                            val intent = Intent(
                                Intent.ACTION_VIEW,
                                Uri.parse("https://github.com/Anxhul10/contest-notifier-apk/releases/latest")
                            )
                            context.startActivity(intent)
                        },
                    shape = RoundedCornerShape(10.dp)
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 10.dp, vertical = 5.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column(modifier = Modifier.weight(1f)) {
                            Text(
                                text = "Update Available!",
                                color = if (isSystemInDarkTheme()) Color(0xFFB9E5D8) else Color(0xFF245B50),
                                fontWeight = FontWeight.Bold,
                                style = MaterialTheme.typography.bodyMedium
                            )
                            Text(
                                text = "Download the latest version from GitHub Releases.",
                                color = if (isSystemInDarkTheme()) Color(0xFFA8CFC4) else Color(0xFF426B62),
                                fontWeight = FontWeight.Normal,
                                style = MaterialTheme.typography.labelSmall
                            )
                        }
                    }
                }
            }
            if(names.isEmpty()) {
                CircularProgressIndicator();
                Text(
                    text = "Getting things ready...",
                    modifier = Modifier.fillMaxWidth(),
                    textAlign = TextAlign.Center,
                    color = MaterialTheme.colorScheme.primary,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold,
                    fontFamily = headingFontFamily
                )

                Text(
                    text = "We're fetching the latest contests. The server may take "
                            + "a little longer to respond after being inactive.",
                    modifier = Modifier.fillMaxWidth(),
                    textAlign = TextAlign.Center,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    fontSize = 13.sp,
                    lineHeight = 18.sp,
                    fontFamily = bodyFontFamily
                )
            } else {
                CardWrapper(modifier, names, isLoading)
            }
        }
    }
}