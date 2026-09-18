package com.example.contest_notifier_apk

import androidx.compose.material3.Text
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import com.example.contest_notifier_apk.ui.theme.ContestnotifierapkTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
//        enableEdgeToEdge()
        setContent {
            ContestnotifierapkTheme {
                ContestNotifierScreen()
            }
        }
    }
}
@Composable
fun ContestNotifierScreen() {
    val numbers = listOf(1,2,3)

    var currentIdx by remember {
        mutableStateOf(0)
    }

    Surface(
        modifier = Modifier.fillMaxSize(),
        color = MaterialTheme.colorScheme.background
    ) {

        Column {
            Text(
                text = numbers[currentIdx].toString()
            )
            Button(
                onClick = {
                    currentIdx = (currentIdx + 1) % numbers.size
                }
            ) {
                Text(text = "click me")
            }
        }
    }
}
