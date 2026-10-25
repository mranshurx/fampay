package com.venice.fakepay.ui

import androidx.compose.animation.core.*
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.*
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalClipboardManager
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.venice.fakepay.data.Txn
import com.venice.fakepay.data.formatDateTime
import com.venice.fakepay.data.formatInr
import com.venice.fakepay.ui.theme.*

@Composable
fun SuccessScreen(txn: Txn, onDone: () -> Unit) {
    val clipboard = LocalClipboardManager.current
    val pop = remember { Animatable(0.6f) }
    LaunchedEffect(Unit) {
        pop.animateTo(1f, spring(dampingRatio = Spring.DampingRatioMediumBouncy))
    }

    Box(Modifier.fillMaxSize().background(Color(0xFF0B0B0C))) {

        Column(
            Modifier
                .fillMaxSize()
                .statusBarsPadding()
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 20.dp)
        ) {
            Spacer(Modifier.height(8.dp))

            Row(verticalAlignment = Alignment.CenterVertically) {
                DarkCircleButton(Icons.Rounded.Close, onDone)
                Spacer(Modifier.weight(1f))
                DarkCircleButton(Icons.Rounded.HelpOutline) {}
                Spacer(Modifier.width(10.dp))
                DarkCircleButton(Icons.Rounded.Share) {}
            }

            Spacer(Modifier.height(36.dp))

            Column(
                Modifier.fillMaxWidth().scale(pop.value),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    formatInr(txn.amount),
                    color = Yellow,
                    fontSize = 52.sp,
                    fontWeight = FontWeight.Black
                )
                Spacer(Modifier.height(6.dp))
                Text("from ${txn.name}", color = White, fontSize = 17.sp, fontWeight = FontWeight.SemiBold)
                Spacer(Modifier.height(2.dp))
                Text(txn.vpa, color = White.copy(alpha = 0.5f), fontSize = 14.sp)
                Spacer(Modifier.height(16.dp))
                Box(
                    Modifier
                        .clip(RoundedCornerShape(50))
                        .background(White.copy(alpha = 0.1f))
                        .padding(horizontal = 16.dp, vertical = 8.dp)
                ) {
                    Text("Paid via ${txn.via}", color = White, fontSize = 13.sp)
                }
            }

            Spacer(Modifier.height(34.dp))

            // Details card
            Column(
                Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(20.dp))
                    .background(White.copy(alpha = 0.05f))
                    .padding(18.dp)
            ) {
                Text("To FamX wallet", color = White, fontSize = 15.sp, fontWeight = FontWeight.SemiBold)
                Spacer(Modifier.height(2.dp))
                Text("on ${formatDateTime(txn.time)}", color = White.copy(alpha = 0.5f), fontSize = 13.sp)

                Spacer(Modifier.height(14.dp))
                CopyLine("UTR", txn.utr) { clipboard.setText(AnnotatedString(txn.utr)) }
                Spacer(Modifier.height(8.dp))
                CopyLine("TXN ID", txn.id) { clipboard.setText(AnnotatedString(txn.id)) }

                Spacer(Modifier.height(18.dp))

                TimelineRow("Payment initiated", true)
                TimelineRow("Money received successfully", true)
                TimelineRow("Rewards credited", true, last = true)
            }

            Spacer(Modifier.height(20.dp))

            Text(
                "This was a fake payment. No real money moved.",
                color = White.copy(alpha = 0.4f),
                fontSize = 12.sp
            )

            Spacer(Modifier.height(120.dp))
        }

        // Bottom sheet
        Column(
            Modifier
                .align(Alignment.BottomCenter)
                .fillMaxWidth()
                .clip(RoundedCornerShape(topStart = 26.dp, topEnd = 26.dp))
                .background(White)
                .navigationBarsPadding()
                .padding(20.dp)
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Box(
                    Modifier.size(38.dp).clip(CircleShape).background(YellowSoft),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(Icons.Rounded.EmojiEvents, contentDescription = null, tint = YellowDeep)
                }
                Spacer(Modifier.width(12.dp))
                Column(Modifier.weight(1f)) {
                    Text("You earned 2 FamCoins", fontWeight = FontWeight.ExtraBold, fontSize = 15.sp, color = Ink)
                    Text("Scratch to win more", fontSize = 12.sp, color = Grey)
                }
            }
            Spacer(Modifier.height(14.dp))
            androidx.compose.material3.Button(
                onClick = onDone,
                modifier = Modifier.fillMaxWidth().height(54.dp),
                shape = RoundedCornerShape(18.dp),
                colors = androidx.compose.material3.ButtonDefaults.buttonColors(
                    containerColor = Yellow,
                    contentColor = Black
                )
            ) {
                Text("Done", fontSize = 17.sp, fontWeight = FontWeight.ExtraBold)
            }
        }
    }
}

@Composable
private fun CopyLine(label: String, value: String, onCopy: () -> Unit) {
    Row(verticalAlignment = Alignment.CenterVertically) {
        Text("$label: ", color = White.copy(alpha = 0.5f), fontSize = 13.sp)
        Text(value, color = White, fontSize = 13.sp, fontWeight = FontWeight.Medium)
        Spacer(Modifier.width(8.dp))
        Icon(
            Icons.Rounded.ContentCopy,
            contentDescription = "Copy",
            tint = White.copy(alpha = 0.6f),
            modifier = Modifier.size(15.dp).clip(CircleShape).clickable2(onCopy)
        )
    }
}

@Composable
private fun TimelineRow(text: String, done: Boolean, last: Boolean = false) {
    Row {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Box(
                Modifier.size(22.dp).clip(CircleShape).background(if (done) Green else Grey),
                contentAlignment = Alignment.Center
            ) {
                Icon(Icons.Rounded.Check, contentDescription = null, tint = White, modifier = Modifier.size(14.dp))
            }
            if (!last) {
                Box(Modifier.width(2.dp).height(26.dp).background(Green))
            }
        }
        Spacer(Modifier.width(12.dp))
        Text(
            text,
            color = White,
            fontSize = 14.sp,
            modifier = Modifier.padding(top = 1.dp, bottom = if (last) 0.dp else 12.dp)
        )
    }
}
