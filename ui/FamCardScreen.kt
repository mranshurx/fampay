package com.venice.fakepay.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.venice.fakepay.data.Txn
import com.venice.fakepay.ui.theme.*

@Composable
fun FamCardScreen(transactions: List<Txn>) {
    var frozen by remember { mutableStateOf(false) }
    var online by remember { mutableStateOf(true) }

    Column(
        Modifier.fillMaxSize().verticalScroll(rememberScrollState()).padding(horizontal = 16.dp)
    ) {
        Spacer(Modifier.height(10.dp))

        // The card itself
        Column(
            Modifier
                .fillMaxWidth()
                .height(210.dp)
                .clip(RoundedCornerShape(24.dp))
                .background(Brush.linearGradient(listOf(Color(0xFF1C1C1E), Color(0xFF000000))))
                .padding(20.dp)
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text("FamX", color = Yellow, fontWeight = FontWeight.Black, fontSize = 20.sp)
                Spacer(Modifier.weight(1f))
                Pill(if (frozen) "FROZEN" else "ACTIVE", if (frozen) White else Yellow, Black)
            }
            Spacer(Modifier.weight(1f))
            Text("\u2022\u2022\u2022\u2022  \u2022\u2022\u2022\u2022  \u2022\u2022\u2022\u2022  4821",
                color = White, fontSize = 17.sp, letterSpacing = 1.sp)
            Spacer(Modifier.height(10.dp))
            Row {
                Column {
                    Text("CARD HOLDER", color = White.copy(alpha = 0.45f), fontSize = 9.sp)
                    Text("GUEST USER", color = White, fontSize = 12.sp, fontWeight = FontWeight.SemiBold)
                }
                Spacer(Modifier.width(28.dp))
                Column {
                    Text("VALID THRU", color = White.copy(alpha = 0.45f), fontSize = 9.sp)
                    Text("12/29", color = White, fontSize = 12.sp, fontWeight = FontWeight.SemiBold)
                }
            }
        }

        Spacer(Modifier.height(16.dp))

        FamCardSurface {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Column(Modifier.weight(1f)) {
                    Text("Freeze card", fontWeight = FontWeight.Bold, color = Ink)
                    Text("Block all transactions instantly", color = Grey, fontSize = 12.sp)
                }
                Switch(checked = frozen, onCheckedChange = { frozen = it })
            }
            Spacer(Modifier.height(12.dp))
            HorizontalDivider(color = GreyLine)
            Spacer(Modifier.height(12.dp))
            Row(verticalAlignment = Alignment.CenterVertically) {
                Column(Modifier.weight(1f)) {
                    Text("Online payments", fontWeight = FontWeight.Bold, color = Ink)
                    Text("Allow e-commerce payments", color = Grey, fontSize = 12.sp)
                }
                Switch(checked = online, onCheckedChange = { online = it })
            }
        }

        Spacer(Modifier.height(12.dp))

        FamCardSurface {
            CardRow(Icons.Rounded.Pin, "Reset PIN")
            HorizontalDivider(color = GreyLine)
            CardRow(Icons.Rounded.Palette, "Change card design")
            HorizontalDivider(color = GreyLine)
            CardRow(Icons.Rounded.LocalAtm, "ATM withdrawals", "On")
            HorizontalDivider(color = GreyLine)
            CardRow(Icons.Rounded.CreditScore, "Card limits", "\u20B9 50,000/day")
        }

        Spacer(Modifier.height(20.dp))
    }
}

@Composable
private fun CardRow(icon: androidx.compose.ui.graphics.vector.ImageVector, title: String, trailing: String = "") {
    Row(Modifier.fillMaxWidth().padding(vertical = 12.dp), verticalAlignment = Alignment.CenterVertically) {
        Icon(icon, contentDescription = null, tint = Ink, modifier = Modifier.size(20.dp))
        Spacer(Modifier.width(14.dp))
        Text(title, color = Ink, fontSize = 14.sp, modifier = Modifier.weight(1f))
        Text(trailing, color = Grey, fontSize = 12.sp)
    }
}
