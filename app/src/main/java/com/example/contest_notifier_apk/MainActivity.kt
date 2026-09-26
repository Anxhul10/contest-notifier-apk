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
@Composable
fun ContestList(modifier: Modifier = Modifier, names: List<String> = listOf("contest 1", "contest 2", "contest 3", "contest 4", "contest 5", "contest 6", "contest 7", "contest 8", "contest 9")) {
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