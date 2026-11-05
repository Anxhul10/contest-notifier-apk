package com.anxhul10.contest_notifier_apk.utils

import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.googlefonts.Font
import androidx.compose.ui.text.googlefonts.GoogleFont
import com.anxhul10.contest_notifier_apk.R

val provider = GoogleFont.Provider(
    providerAuthority = "com.google.android.gms.fonts",
    providerPackage = "com.google.android.gms",
    certificates = R.array.com_google_android_gms_fonts_certs
)

val manrope = GoogleFont("Manrope")

val headingFontFamily = FontFamily(
    Font(
        googleFont = manrope,
        fontProvider = provider,
        weight = FontWeight.Bold
    ),
    Font(
        googleFont = manrope,
        fontProvider = provider,
        weight = FontWeight.SemiBold
    )
)

// Body font
val inter = GoogleFont("Inter")

val bodyFontFamily = FontFamily(
    Font(
        googleFont = inter,
        fontProvider = provider,
        weight = FontWeight.Normal
    ),
    Font(
        googleFont = inter,
        fontProvider = provider,
        weight = FontWeight.Medium
    )
)

val fontName = GoogleFont("Lobster Two")

val fontFamily = FontFamily(
    Font(
        googleFont = fontName,
        fontProvider = provider,
        weight = FontWeight.Thin,
        style = FontStyle.Italic
    )
)