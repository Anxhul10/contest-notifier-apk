package com.example.contest_notifier_apk.ui.components

import androidx.compose.animation.core.animateDpAsState
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ElevatedButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.googlefonts.Font
import androidx.compose.ui.unit.dp
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import kotlinx.coroutines.delay
import com.example.contest_notifier_apk.utils.fontFamily

@Composable
fun Card(title: String,platform: String, contestTime: Int,relativeTimeSeconds: Long, modifier: Modifier = Modifier) {
    var expanded = remember { mutableStateOf(false)};
    var remainingSeconds by remember(relativeTimeSeconds) {
        mutableStateOf(-relativeTimeSeconds);
    }

    LaunchedEffect(relativeTimeSeconds) {
        while(remainingSeconds > 0) {
            delay(1000)
            remainingSeconds--;
        }
    }
    var extraPadding = animateDpAsState(
        if (expanded.value) 48.dp else 0.dp,
    )
    Surface(
        color = MaterialTheme.colorScheme.primaryContainer,
        modifier = modifier.padding(vertical = 4.dp, horizontal = 6.dp),
        shape = RoundedCornerShape(15.dp)
    ) {
        Row(modifier = Modifier.padding(24.dp)) {
            Column(modifier = Modifier
                .weight(1f)
                .padding(bottom = extraPadding.value)
            ) {
                Text(text = title, fontWeight = FontWeight.ExtraBold);
                val hr = contestTime / (60*60);
                Text(text = hr.toString()+" hr", fontWeight = FontWeight.ExtraLight, fontStyle = FontStyle.Italic)
                Text(
                    text = if (remainingSeconds > 0) {
                        val days = remainingSeconds / 86400
                        val hours = (remainingSeconds % 86400) / 3600
                        val minutes = (remainingSeconds % 3600) / 60
                        val seconds = remainingSeconds % 60

                        "${days}d ${hours}h ${minutes}m ${seconds}s"
                    } else {
                        "Started"
                    }
                )
            }
            ElevatedButton(
                onClick = {
                    expanded.value = expanded.value;
                }
            ) {
                Text(platform);
//                Text(if (expanded.value) "show less..." else "show more...")
            }
        }
    }
}