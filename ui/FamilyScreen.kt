package com.venice.fakepay.ui

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.PersonAdd
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.venice.fakepay.data.Txn
import com.venice.fakepay.data.formatInr
import com.venice.fakepay.ui.theme.*

private data class Member(val name: String, val emoji: String, val relation: String)

private val members = listOf(
    Member("Papa", "\uD83D\uDC68", "Added \u20B92,000 this month"),
    Member("Mummy", "\uD83D\uDC69", "Added \u20B91,500 this month"),
    Member("Salke's", "\uD83D\uDC3C", "Friend \u2022 12 payments"),
    Member("Aditi", "\uD83D\uDE0E", "Friend \u2022 8 payments")
)

@Composable
fun FamilyScreen(transactions: List<Txn>) {
    val incoming = transactions.filter { it.direction == "RECEIVED" }.sumOf { it.amount }

    Column(
        Modifier.fillMaxSize().verticalScroll(rememberScrollState()).padding(horizontal = 16.dp)
    ) {
        Spacer(Modifier.height(14.dp))
        Text("Family", fontWeight = FontWeight.Black, fontSize = 26.sp, color = Ink)
        Text("Pocket money, splits and everything in between", color = Grey, fontSize = 13.sp)

        Spacer(Modifier.height(18.dp))

        FamCardSurface {
            Text("Received this month", color = Grey, fontSize = 12.sp)
            Spacer(Modifier.height(4.dp))
            Text(formatInr(incoming), fontWeight = FontWeight.Black, fontSize = 28.sp, color = Green)
        }

        Spacer(Modifier.height(18.dp))

        Text("Your people", fontWeight = FontWeight.ExtraBold, fontSize = 16.sp, color = Ink)
        Spacer(Modifier.height(10.dp))

        FamCardSurface {
            members.forEachIndexed { i, m ->
                Row(Modifier.fillMaxWidth().padding(vertical = 12.dp), verticalAlignment = Alignment.CenterVertically) {
                    EmojiAvatar(m.emoji, 44)
                    Spacer(Modifier.width(12.dp))
                    Column(Modifier.weight(1f)) {
                        Text(m.name, fontWeight = FontWeight.Bold, fontSize = 15.sp, color = Ink)
                        Text(m.relation, fontSize = 12.sp, color = Grey)
                    }
                    Text("Pay", color = Black, fontWeight = FontWeight.Bold, fontSize = 13.sp)
                }
                if (i != members.lastIndex) HorizontalDivider(color = GreyLine)
            }
        }

        Spacer(Modifier.height(14.dp))

        FamCardSurface {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(Icons.Rounded.PersonAdd, contentDescription = null, tint = Ink)
                Spacer(Modifier.width(14.dp))
                Column {
                    Text("Invite to FamPay", fontWeight = FontWeight.Bold, color = Ink)
                    Text("Both of you get 500 FamCoins", color = Grey, fontSize = 12.sp)
                }
            }
        }

        Spacer(Modifier.height(30.dp))
    }
}
