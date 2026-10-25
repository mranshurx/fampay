package com.venice.fakepay.ui.theme

import androidx.compose.material3.Typography
import androidx.compose.ui.text.ExperimentalTextApi
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.googlefonts.GoogleFont
import androidx.compose.ui.unit.sp
import com.venice.fakepay.R

@OptIn(ExperimentalTextApi::class)
private val provider = GoogleFont.Provider(
    providerAuthority = "com.google.android.gms.fonts",
    providerPackage = "com.google.android.gms",
    certificates = R.array.com_google_android_gms_fonts_certs
)

@OptIn(ExperimentalTextApi::class)
private fun famFont(weight: FontWeight) =
    Font(GoogleFont("Poppins"), provider, weight)

private val Fam = FontFamily(
    famFont(FontWeight.Normal),
    famFont(FontWeight.Medium),
    famFont(FontWeight.SemiBold),
    famFont(FontWeight.Bold),
    famFont(FontWeight.ExtraBold)
)

val FamTypography = Typography().let { base ->
    base.copy(
        displayLarge = base.displayLarge.copy(fontFamily = Fam, fontWeight = FontWeight.ExtraBold, fontSize = 44.sp),
        headlineLarge = base.headlineLarge.copy(fontFamily = Fam, fontWeight = FontWeight.ExtraBold, fontSize = 30.sp),
        headlineMedium = base.headlineMedium.copy(fontFamily = Fam, fontWeight = FontWeight.Bold, fontSize = 24.sp),
        titleLarge = base.titleLarge.copy(fontFamily = Fam, fontWeight = FontWeight.Bold, fontSize = 20.sp),
        titleMedium = base.titleMedium.copy(fontFamily = Fam, fontWeight = FontWeight.SemiBold, fontSize = 16.sp),
        bodyLarge = base.bodyLarge.copy(fontFamily = Fam, fontSize = 15.sp),
        bodyMedium = base.bodyMedium.copy(fontFamily = Fam, fontSize = 14.sp),
        bodySmall = base.bodySmall.copy(fontFamily = Fam, fontSize = 12.sp),
        labelLarge = base.labelLarge.copy(fontFamily = Fam, fontWeight = FontWeight.SemiBold, fontSize = 14.sp),
        labelSmall = base.labelSmall.copy(fontFamily = Fam, fontSize = 11.sp)
    )
}
