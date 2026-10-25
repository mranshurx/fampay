package com.venice.fakepay.ui

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.*
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.venice.fakepay.data.Txn
import com.venice.fakepay.data.formatInrShort
import com.venice.fakepay.ui.theme.*

private data class Contact(val name: String, val emoji: String)

private val contacts = listOf(
    Contact("Salke's", "\uD83D\uDC3C"),
    Contact("Aditi", "\uD83D\uDE0E"),
    Contact("Anjor", "\uD83E\uDD2A"),
    Contact("Kabir", "\uD83E\uDD8A")
)

@Composable
fun HomeScreen(
    transactions: List<Txn>,
    coins: Int,
    onScan: () -> Unit,
    onManual: () -> Unit,
    onHistory: () -> Unit,
    onFamCard: () -> Unit,
    onFamCoins: () -> Unit,
    onProfile: () -> Unit
) {
    val context = LocalContext.current

    Column(
        Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(horizontal = 16.dp)
    ) {
        Spacer(Modifier.height(10.dp))

        // ---- Top bar: globe | scan ... bell | emoji avatar
        Row(verticalAlignment = Alignment.CenterVertically) {
            TopIcon(Icons.Rounded.Language) {}
            Spacer(Modifier.width(10.dp))
            TopIcon(Icons.Rounded.QrCodeScanner, onScan)
            Spacer(Modifier.weight(1f))
            TopIcon(Icons.Rounded.NotificationsNone) {}
            Spacer(Modifier.width(10.dp))
            Box(Modifier.clickable { onProfile() }) { EmojiAvatar("\uD83D\uDE0E", 40) }
        }

        Spacer(Modifier.height(18.dp))

        // ---- Balance hero (black card, gold amount) — FamPay dark aesthetic
        Column(
            Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(24.dp))
                .background(Brush.linearGradient(listOf(Color(0xFF1A1A1C), Black)))
                .padding(20.dp)
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text("FamX wallet", color = White.copy(alpha = 0.7f), fontSize = 12.sp)
                Spacer(Modifier.weight(1f))
                Pill("UNLIMITED", Yellow, Black)
            }
            Spacer(Modifier.height(8.dp))
            Text("\u20B9 99,99,999", color = Yellow, fontSize = 40.sp, fontWeight = FontWeight.Black)
            Spacer(Modifier.height(4.dp))
            Text("Never runs out. Really.", color = White.copy(alpha = 0.55f), fontSize = 12.sp)
            Spacer(Modifier.height(16.dp))
            Row(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                Box(
                    Modifier
                        .clip(RoundedCornerShape(50))
                        .background(Yellow)
                        .clickable {
                            Toast.makeText(context, "Added \u20B910,000 (fake)", Toast.LENGTH_SHORT).show()
                        }
                        .padding(horizontal = 18.dp, vertical = 9.dp)
                ) {
                    Text("+ Add money", color = Black, fontWeight = FontWeight.Bold, fontSize = 13.sp)
                }
                Box(
                    Modifier
                        .clip(RoundedCornerShape(50))
                        .background(White.copy(alpha = 0.14f))
                        .clickable { onScan() }
                        .padding(horizontal = 18.dp, vertical = 9.dp)
                ) {
                    Text("Scan & Pay", color = White, fontWeight = FontWeight.Bold, fontSize = 13.sp)
                }
            }
        }

        Spacer(Modifier.height(22.dp))

        // ---- Pay to recents
        Text("Pay to recents", fontWeight = FontWeight.ExtraBold, fontSize = 16.sp, color = Ink)
        Spacer(Modifier.height(10.dp))

        LazyRow(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
            item {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Box(
                        Modifier
                            .size(58.dp)
                            .clip(RoundedCornerShape(18.dp))
                            .background(Yellow)
                            .clickable { onScan() },
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(Icons.Rounded.QrCodeScanner, contentDescription = "Scan QR", tint = Black)
                    }
                    Spacer(Modifier.height(6.dp))
                    Text("Scan QR", fontSize = 11.sp, color = Ink, fontWeight = FontWeight.SemiBold)
                }
            }
            items(contacts) { c ->
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Box(Modifier.clickable { onManual() }) { EmojiAvatar(c.emoji, 52) }
                    Spacer(Modifier.height(4.dp))
                    Text(c.name, fontSize = 11.sp, color = Ink, fontWeight = FontWeight.Medium)
                }
            }
        }

        Spacer(Modifier.height(24.dp))

        // ---- For you
        Text("For you \uD83D\uDE0E", fontWeight = FontWeight.ExtraBold, fontSize = 16.sp, color = Ink)
        Spacer(Modifier.height(4.dp))

        ForYouRow(
            icon = Icons.Rounded.ReceiptLong,
            iconBg = Color(0xFFFFE9DF),
            iconTint = Orange,
            title = "Your transactions",
            subtitle = "View all your transactions",
            onClick = onHistory
        )
        Spacer(Modifier.height(10.dp))
        ForYouRow(
            icon = Icons.Rounded.MonetizationOn,
            iconBg = YellowSoft,
            iconTint = YellowDeep,
            title = "FamCoins",
            subtitle = "$coins coins \u2022 scratch and win more",
            onClick = onFamCoins
        )
        Spacer(Modifier.height(10.dp))
        ForYouRow(
            icon = Icons.Rounded.CreditCard,
            iconBg = Color(0xFFE7F0FF),
            iconTint = Color(0xFF2D6CDF),
            title = "FamX Card",
            subtitle = "Numberless card, zero worries",
            onClick = onFamCard
        )

        Spacer(Modifier.height(24.dp))

        // ---- To-do list
        Text("To-do list \uD83D\uDCDD", fontWeight = FontWeight.ExtraBold, fontSize = 16.sp, color = Ink)
        Spacer(Modifier.height(10.dp))

        LazyRow(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
            item {
                TodoCard(
                    emoji = "\uD83D\uDCB3",
                    title = "Order your FamX card",
                    subtitle = "Free, delivered home",
                    colors = listOf(Color(0xFFFFB07C), Color(0xFFFF7A45))
                )
            }
            item {
                TodoCard(
                    emoji = "\uD83C\uDFAF",
                    title = "Set a savings goal",
                    subtitle = "Save \u20B9500 this week",
                    colors = listOf(Color(0xFF9BD8FF), Color(0xFF4FA9F5))
                )
            }
            item {
                TodoCard(
                    emoji = "\uD83C\uDF81",
                    title = "Scratch your card",
                    subtitle = "Win up to 500 FamCoins",
                    colors = listOf(Color(0xFFFFE07A), Color(0xFFFFC629))
                )
            }
        }

        Spacer(Modifier.height(24.dp))

        // ---- Recent activity
        SectionHeader("Recent activity") {
            Text(
                "See all",
                color = Black,
                fontSize = 13.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.clickable { onHistory() }
            )
        }

        if (transactions.isEmpty()) {
            FamCardSurface { Text("No transactions yet", color = Grey) }
        } else {
            FamCardSurface {
                transactions.take(5).forEachIndexed { i, t ->
                    TxnRow(t, showDivider = i != 4)
                }
            }
        }

        Spacer(Modifier.height(90.dp))
    }
}

@Composable
private fun TopIcon(
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    onClick: () -> Unit
) {
    Box(
        Modifier.size(40.dp).clip(CircleShape).clickable { onClick() },
        contentAlignment = Alignment.Center
    ) {
        Icon(icon, contentDescription = null, tint = Ink, modifier = Modifier.size(22.dp))
    }
}

@Composable
private fun TodoCard(
    emoji: String,
    title: String,
    subtitle: String,
    colors: List<Color>
) {
    Column(
        Modifier
            .width(190.dp)
            .clip(RoundedCornerShape(22.dp))
            .background(Brush.linearGradient(colors))
            .padding(16.dp)
    ) {
        Text(emoji, fontSize = 30.sp)
        Spacer(Modifier.height(24.dp))
        Text(title, fontWeight = FontWeight.ExtraBold, fontSize = 15.sp, color = Color(0xFF1A1A1A))
        Spacer(Modifier.height(2.dp))
        Text(subtitle, fontSize = 11.sp, color = Color(0xFF1A1A1A).copy(alpha = 0.65f))
    }
}
