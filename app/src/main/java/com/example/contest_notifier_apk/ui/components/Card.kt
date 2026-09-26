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
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun Card(name: String, modifier: Modifier = Modifier) {
    var expanded = remember { mutableStateOf(false)};
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
                Text(text = name);
            }
            ElevatedButton(
                onClick = {
                    expanded.value = !expanded.value;
                }
            ) {
                Text(if (expanded.value) "show less..." else "show more...")
            }
        }
    }
}