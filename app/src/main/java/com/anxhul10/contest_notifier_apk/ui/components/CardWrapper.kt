package com.anxhul10.contest_notifier_apk.ui.components

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.anxhul10.contest_notifier_apk.ContestData
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.ui.Alignment
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.sp
import com.anxhul10.contest_notifier_apk.utils.bodyFontFamily
import com.anxhul10.contest_notifier_apk.utils.headingFontFamily

@Composable
fun CardWrapper(
    modifier: Modifier = Modifier,
    Contests: List<ContestData>,
    isLoading: Boolean
) {
    LazyColumn() {
        if(isLoading) {
            item {
                Box(
                    modifier = Modifier.fillMaxWidth(),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator()
                }
                Text(
                    text = "Waking up the contest server...",
                    modifier = Modifier.fillMaxWidth(),
                    textAlign = TextAlign.Center,
                    color = MaterialTheme.colorScheme.primary,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold,
                    fontFamily = headingFontFamily
                )

                Text(
                    text = "The server may take a little longer to respond after being inactive. Your saved contests are available while we fetch the latest updates.",
                    modifier = Modifier.fillMaxWidth(),
                    textAlign = TextAlign.Center,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    fontSize = 13.sp,
                    lineHeight = 18.sp,
                    fontWeight = FontWeight.Normal,
                    fontFamily = bodyFontFamily
                )
            }
        }
        items(items = Contests) {
            Contest -> Card(
                    title = Contest.name,
                    platform = Contest.platform,
                    contestDuration = Contest.contestDuration,
                    relativeTimeSeconds = Contest.relativeTimeSeconds
                )
        }
    }
}