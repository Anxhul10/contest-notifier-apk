package com.example.contest_notifier_apk.ui.components

import android.content.Intent
import android.net.Uri
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import com.example.contest_notifier_apk.MyApp
import com.example.contest_notifier_apk.R
import com.example.contest_notifier_apk.ui.theme.Purple40
import com.example.contest_notifier_apk.utils.fontFamily

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
                            fontFamily = fontFamily
                        )
                        Text(
                            text = " Notifier",
                            fontSize = 20.sp,
                            fontWeight = FontWeight.Bold,
                            letterSpacing = 0.5.sp,
                            color = Color.Black,
                            fontFamily = fontFamily
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