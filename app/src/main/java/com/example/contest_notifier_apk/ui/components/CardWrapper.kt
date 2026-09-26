package com.example.contest_notifier_apk.ui.components

import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

@Composable
fun CardWrapper(
    modifier: Modifier = Modifier,
    names: List<String>
) {
    LazyColumn() {
        items(items = names) {
                name -> Card(name = name)
        }
    }
}