package com.venice.fakepay.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.venice.fakepay.ui.theme.Black
import com.venice.fakepay.ui.theme.Yellow
import kotlinx.coroutines.delay

@Composable
fun SplashScreen(onDone: () -> Unit) {
    LaunchedEffect(Unit) {
        delay(1300)
        onDone()
    }

    Box(
        Modifier.fillMaxSize().background(Yellow),
        contentAlignment = Alignment.Center
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Text("FamPay", fontSize = 44.sp, fontWeight = FontWeight.Black, color = Black)
            Spacer(Modifier.height(6.dp))
            Text("fake money. real vibes.", fontSize = 13.sp, color = Black.copy(alpha = 0.6f))
        }

        Column(
            Modifier.align(Alignment.BottomCenter).padding(bottom = 48.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text("Powered by Trio (kinda)", fontSize = 11.sp, color = Black.copy(alpha = 0.5f))
        }
    }
}
