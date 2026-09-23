package com.example.contest_notifier_apk

import android.content.Intent
import android.content.res.Configuration
import android.graphics.pdf.content.PdfPageGotoLinkContent
import android.net.Uri
import androidx.compose.ui.platform.LocalContext
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.spring
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ElevatedButton
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.contest_notifier_apk.ui.theme.ContestnotifierapkTheme
import com.example.contest_notifier_apk.ui.theme.Pink40
import com.example.contest_notifier_apk.ui.theme.Purple40
import kotlinx.coroutines.delay

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


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TopBar() {
    val context = LocalContext.current
    Scaffold(
        topBar = {
            TopAppBar(
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Purple40,
                    titleContentColor = Color.Black,
                ),
                title = {
                    Row() {
                        Text(
                            text = "Contest",
                            fontSize = 20.sp,
                            fontWeight = FontWeight.Bold,
                            letterSpacing = 0.5.sp,
                            color = MaterialTheme.colorScheme.primary,
                            fontFamily = FontFamily.Cursive
                        )
                        Text(
                            text = " Notifier",
                            fontSize = 20.sp,
                            fontWeight = FontWeight.Bold,
                            letterSpacing = 0.5.sp,
                            color = Color.Black,
                            fontFamily = FontFamily.Cursive
                        )
                    }
                },
                actions = {
                    IconButton(onClick = {
                        println("github clicked")
                        val intent = Intent(
                            Intent.ACTION_VIEW,
                            Uri.parse("https://github.com/Anxhul10")
                        )

                        context.startActivity(intent)
                    }) {
                        Icon(
                            painter = painterResource(id = R.drawable.github),
                            tint = MaterialTheme.colorScheme.primary,
                            contentDescription = "Current Speed",
                        )
                    }
                }
            )
        },

    ) {
        innerPadding ->
        MyApp(Modifier.padding((innerPadding)))
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

@Composable
fun MyApp(modifier: Modifier = Modifier, names: List<String> = listOf("user 1", "user 2", "user 3")) {
    Surface (
        modifier = modifier,
        color = MaterialTheme.colorScheme.background
    ) {
        Column () {
            for(name in names) {
                Greetings(modifier)
            }
        }
    }
}

@Composable
fun Greetings(
    modifier: Modifier = Modifier,
    names: List<String> = List(10) {"$it"}
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
        modifier = modifier.padding(vertical = 4.dp, horizontal = 2.dp),
        shape = RoundedCornerShape(15.dp)
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