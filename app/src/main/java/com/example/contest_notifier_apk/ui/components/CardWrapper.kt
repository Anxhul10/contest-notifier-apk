package com.example.contest_notifier_apk.ui.components

import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.example.contest_notifier_apk.ContestData

@Composable
fun CardWrapper(
    modifier: Modifier = Modifier,
    Contests: List<ContestData>
) {
    LazyColumn() {
        items(items = Contests) {
                Contest -> Card(
                    title = Contest.title,
                    platform = Contest.platform,
                    contestTime = Contest.contestTime
                )
        }
    }
}