package com.venice.fakepay.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.MonetizationOn
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.venice.fakepay.ui.theme.*

@Composable
fun FamCoinsScreen(coins: Int, onScratch: () -> Unit) {
    var scratched by remember { mutableStateOf(false) }

    Column(
        Modifier.fillMaxSize().verticalScroll(rememberScrollState()).padding(horizontal = 16.dp)
    ) {
        Spacer(Modifier.height(10.dp))

        Column(
            Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(24.dp))
                .background(Brush.linearGradient(listOf(Yellow, YellowDeep)))
                .padding(22.dp)
        ) {
            Text("Your FamCoins", color = Black.copy(alpha = 0.65f), fontSize = 13.sp)
            Spacer(Modifier.height(6.dp))
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text("$coins", color = Black, fontSize = 44.sp, fontWeight = FontWeight.Black)
                Spacer(Modifier.width(8.dp))
                Text("coins", color = Black.copy(alpha = 0.6f), fontSize = 15.sp, fontWeight = FontWeight.Bold)
            }
            Spacer(Modifier.height(10.dp))
            Text("1 FamCoin = \u20B91 at FamStore", color = Black.copy(alpha = 0.7f), fontSize = 12.sp)
        }

        Spacer(Modifier.height(20.dp))

        Text("Daily scratch card \uD83C\uDF81", fontWeight = FontWeight.ExtraBold, fontSize = 16.sp, color = Ink)
        Spacer(Modifier.height(10.dp))

        Box(
            Modifier
                .fillMaxWidth()
                .height(150.dp)
                .clip(RoundedCornerShape(22.dp))
                .background(if (scratched) Color(0xFFE9FBF1) else Color(0xFFEDEDF2))
                .clickable {
                    if (!scratched) {
                        scratched = true
                        onScratch()
                    }
                },
            contentAlignment = Alignment.Center
        ) {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                if (scratched) {
                    Text("\uD83C\uDF89", fontSize = 40.sp)
                    Spacer(Modifier.height(6.dp))
                    Text("+25 FamCoins", fontWeight = FontWeight.Black, fontSize = 20.sp, color = Green)
                    Text("Come back tomorrow for more", color = Grey, fontSize = 12.sp)
                } else {
                    Text("\uD83E\uDE99", fontSize = 40.sp)
                    Spacer(Modifier.height(6.dp))
                    Text("Tap to scratch", fontWeight = FontWeight.Bold, fontSize = 15.sp, color = Ink)
                }
            }
        }

        Spacer(Modifier.height(22.dp))

        Text("Ways to earn", fontWeight = FontWeight.ExtraBold, fontSize = 16.sp, color = Ink)
        Spacer(Modifier.height(10.dp))

        EarnRow("\uD83D\uDCB8", "Pay with FamPay", "2X FamCoins on every payment")
        Spacer(Modifier.height(10.dp))
        EarnRow("\uD83D\uDCF2", "Daily check-in", "10 coins a day")
        Spacer(Modifier.height(10.dp))
        EarnRow("\uD83E\uDD1D", "Refer a friend", "500 coins per friend")
        Spacer(Modifier.height(10.dp))
        EarnRow("\uD83C\uDFAF", "Complete your profile", "50 coins once")

        Spacer(Modifier.height(30.dp))
    }
}

@Composable
private fun EarnRow(emoji: String, title: String, subtitle: String) {
    FamCardSurface(padding = 14) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Box(
                Modifier.size(42.dp).clip(RoundedCornerShape(14.dp)).background(YellowSoft),
                contentAlignment = Alignment.Center
            ) { Text(emoji, fontSize = 20.sp) }
            Spacer(Modifier.width(14.dp))
            Column {
                Text(title, fontWeight = FontWeight.Bold, fontSize = 14.sp, color = Ink)
                Text(subtitle, fontSize = 12.sp, color = Grey)
            }
        }
    }
}
