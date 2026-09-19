package com.example.contest_notifier_apk

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import com.example.contest_notifier_apk.ui.theme.ContestnotifierapkTheme
import kotlinx.coroutines.delay

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            ContestnotifierapkTheme {
                Column {
                    NotificationCard(Notification("Leetcode", "weekly 123", 10))
                }

            }
        }
    }
}

data class Notification(val platform: String, val title: String, val time: Int)
@Composable
fun NotificationCard(data: Notification) {
    Column {
        Text(data.platform, color = Color.Black)
        Text(data.title, color = Color.Black)
        Text(data.time.toString(), color = Color.Black)
    }

}

@Composable
fun ContestNotifierScreen() {

    var remainingTime by remember {
        mutableStateOf(10 * 60) // 10 minutes
    }

    LaunchedEffect(Unit) {
        while (remainingTime > 0) {
            delay(1000)
            remainingTime--
        }
    }

    val minutes = remainingTime / 60
    val seconds = remainingTime % 60

    Surface(
        modifier = Modifier.fillMaxSize(),
        color = MaterialTheme.colorScheme.background
    ) {

        Column {

            Text(
                text = String.format(
                    "%02d:%02d",
                    minutes,
                    seconds
                ),
                style = MaterialTheme.typography.headlineLarge
            )

            Button(
                onClick = {
                    remainingTime = 10 * 60
                }
            ) {
                Text("Restart")
            }
        }
    }
}