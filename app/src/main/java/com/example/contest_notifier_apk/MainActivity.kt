package com.example.contest_notifier_apk

import android.content.res.Configuration
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Column
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.example.contest_notifier_apk.ui.components.CardWrapper
import com.example.contest_notifier_apk.ui.theme.ContestnotifierapkTheme
import com.example.contest_notifier_apk.ui.components.TopBar
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            ContestnotifierapkTheme {
                TopBar()
            }
        }
    }
}

data class ContestData(val title: String, val platform: String, val contestTime: Int)

@Composable
fun ContestList(modifier: Modifier = Modifier, names: List<ContestData> = listOf(
    ContestData("contest 1", "codeforces", 100),
    ContestData("contest 2", "LeetCode", 1000),
    ContestData("contest 3", "codechef", 10000)
)) {
    Surface (
        modifier = modifier,
        color = MaterialTheme.colorScheme.background
    ) {
        Column () {
            CardWrapper(modifier, names)
        }
    }
}

@Preview(
    showBackground = true,
    widthDp = 320,
    uiMode = Configuration.UI_MODE_NIGHT_YES
)
@Composable
fun TopBarPreview() {
    ContestnotifierapkTheme{
        TopBar()
    }
}