package com.venice.fakepay.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

// FamPay brand palette
val Yellow = Color(0xFFFFC629)
val YellowDeep = Color(0xFFF5B301)
val YellowSoft = Color(0xFFFFF3D1)
val Black = Color(0xFF0F0F10)
val Ink = Color(0xFF16161A)
val Grey = Color(0xFF8E8E93)
val GreyLine = Color(0xFFE9E9EC)
val Bg = Color(0xFFF4F4F6)
val White = Color(0xFFFFFFFF)
val Green = Color(0xFF12B76A)
val Orange = Color(0xFFFF6B35)
val Red = Color(0xFFE53935)

private val Scheme = lightColorScheme(
    primary = Yellow,
    onPrimary = Black,
    primaryContainer = YellowSoft,
    onPrimaryContainer = Black,
    secondary = Black,
    onSecondary = Yellow,
    background = Bg,
    onBackground = Ink,
    surface = White,
    onSurface = Ink,
    surfaceVariant = Color(0xFFF1F1F4),
    onSurfaceVariant = Grey,
    outline = GreyLine,
    error = Red
)

@Composable
fun FamPayTheme(content: @Composable () -> Unit) {
    MaterialTheme(colorScheme = Scheme, typography = FamTypography, content = content)
}
