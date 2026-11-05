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
import androidx.compose.ui.unit.dp
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

@Dao
interface ContestDao {
    @Query("SELECT * FROM contests")
    suspend fun getAllContests(): List<Contest>

    @Query("SELECT * FROM contests WHERE id IN (:contestIds)")
    suspend fun loadAllContestByIds(contestIds: IntArray): List<Contest>

    @Query("DELETE FROM contests")
    suspend fun deleteAllContests()

    @Insert
    suspend fun insertAll(contests: List<Contest>)

    @Delete
    suspend fun delete(contest: Contest)
}
@Entity(tableName = "contests")
data class Contest(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val name: String,
    val durationSeconds: Int,
    val startTimeSeconds: Long,
    val platform: String,
    val relativeTimeSeconds: Long
)
@Database(entities = [Contest::class], version = 1)
abstract class AppDatabase : RoomDatabase() {
    abstract fun contestDao(): ContestDao
}
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

        val api: APIService = Retrofit.Builder()
//            .baseUrl("http://127.0.0.1:3000/")
            .baseUrl("https://contest-notifier-apk.onrender.com/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(APIService::class.java)
        val contestStorage = mutableStateListOf<ContestData>();
        val contestLocalStorage = mutableStateListOf<ContestData>();
        val tmpContestStorage = mutableListOf<ContestData>();
        val tmpContestLocalStorage = mutableListOf<ContestData>();
        val db =
            Room.databaseBuilder<AppDatabase>(applicationContext, "database-name")
                .setDriver(AndroidSQLiteDriver())
                .build()
        val contestDao = db.contestDao();
        val updateAvailable = mutableStateOf(true);

        setContent {
            ContestnotifierapkTheme {
                TopBar(contestLocalStorage, contestStorage.size == 0, updateAvailable)
            }
        }


        lifecycleScope.launch {
            val getContestFromStorage = contestDao.getAllContests();
            for(contest in getContestFromStorage) {
                tmpContestLocalStorage.add(
                    ContestData(
                        name = contest.name,
                        platform = contest.platform,
                        contestDuration = contest.durationSeconds,
                        startTimeSeconds = contest.startTimeSeconds,
                        relativeTimeSeconds = System.currentTimeMillis()/1000-contest.startTimeSeconds
                    )
                )
            }
            contestLocalStorage.clear();
            contestLocalStorage.addAll(tmpContestLocalStorage);
            saveContests(this@MainActivity, contestLocalStorage);

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
                            contestDuration = contest.durationSeconds,
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
            val contestToSave = sortedOrder.map {
                data ->
                Contest(
                    name = data.name,
                    durationSeconds = data.contestDuration,
                    startTimeSeconds = data.startTimeSeconds,
                    platform = data.platform,
                    relativeTimeSeconds = data.relativeTimeSeconds
                )
            }
            contestDao.deleteAllContests();
            contestDao.insertAll(contestToSave);
            contestStorage.clear();
            contestStorage.addAll(sortedOrder);
            saveContests(this@MainActivity, contestStorage);
            if(contestStorage.size!=0) {
                contestLocalStorage.clear();
                contestLocalStorage.addAll(contestStorage);
            }
//            usage
//            alarmItem = AlarmItem(
//                time = LocalDateTime.now().plusSeconds(5),
//                message = "hii there"
//            )

            val scheduler = AndroidAlarmScheduler(this@MainActivity)

            contestStorage.forEach { contest ->
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
            contestStorage.forEach { contest ->
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
fun ContestList(modifier: Modifier = Modifier, names: List<ContestData> = emptyList(), isLoading: Boolean, updateAvailable: MutableState<Boolean>) {
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
            } else {
                CardWrapper(modifier, names, isLoading)
            }
        }
    }
}