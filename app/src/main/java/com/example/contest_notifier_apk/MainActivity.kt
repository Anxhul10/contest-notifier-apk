package com.example.contest_notifier_apk

import android.content.res.Configuration
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.spring
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ElevatedButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.contest_notifier_apk.ui.theme.ContestnotifierapkTheme
import kotlinx.coroutines.delay

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            ContestnotifierapkTheme {
                MyApp(modifier = Modifier.fillMaxSize())
            }
        }
    }
}

@Composable
fun MyApp(modifier: Modifier = Modifier, names: List<String> = listOf("user 1", "user 2", "user 3")) {
    Surface (
        modifier = modifier,
        color = MaterialTheme.colorScheme.background
    ) {
        Column (modifier = modifier.padding(vertical = 4.dp)) {
            for(name in names) {
                Greetings(modifier)
            }
        }
    }
}

@Composable
fun Greetings(
    modifier: Modifier = Modifier,
    names: List<String> = List(1000) {"$it"}
) {
    LazyColumn() {
        items(items = names) {
            name -> Greeting(name = name)
        }
    }
}
@Composable
fun Greeting(name: String, modifier: Modifier = Modifier) {
    var expanded = remember { mutableStateOf(false)};
    var extraPadding = animateDpAsState(
        if (expanded.value) 48.dp else 0.dp,
    )
    Surface(
        color = MaterialTheme.colorScheme.primaryContainer,
        modifier = modifier.padding(vertical = 4.dp, horizontal = 8.dp),
        shape = RoundedCornerShape(25.dp)
    ) {
        Row(modifier = Modifier.padding(24.dp)) {
            Column(modifier = Modifier
                .weight(1f)
                .padding(bottom = extraPadding.value)
            ) {
                Text(text = "hello");
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

@Preview(
    showBackground = true,
    widthDp = 320,
    uiMode = Configuration.UI_MODE_NIGHT_YES
)
@Composable
fun GreetingPreview() {
    ContestnotifierapkTheme{
        Greeting("Anshul")
    }
}