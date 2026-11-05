package com.anxhul10.contest_notifier_apk.ui.components

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.anxhul10.contest_notifier_apk.ContestData
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.ui.Alignment

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
            }
        }
        items(items = Contests) {
            Contest -> Card(
                    title = Contest.name,
                    platform = Contest.platform,
                    contestTime = Contest.contestTime,
                    relativeTimeSeconds = Contest.relativeTimeSeconds
                )
        }
    }
}