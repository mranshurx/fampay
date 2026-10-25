package com.venice.fakepay.ui

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.*
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.venice.fakepay.ui.theme.*

@Composable
fun ProfileScreen(txnCount: Int) {
    Column(
        Modifier.fillMaxSize().verticalScroll(rememberScrollState()).padding(horizontal = 16.dp)
    ) {
        Spacer(Modifier.height(10.dp))

        Row(verticalAlignment = Alignment.CenterVertically) {
            EmojiAvatar("\uD83D\uDE0E", 64)
            Spacer(Modifier.width(14.dp))
            Column {
                Text("Guest User", fontWeight = FontWeight.Black, fontSize = 20.sp, color = Ink)
                Text("guest@fam", color = Grey, fontSize = 13.sp)
                Spacer(Modifier.height(6.dp))
                Pill("NO KYC NEEDED", Yellow, Black)
            }
        }

        Spacer(Modifier.height(18.dp))

        FamCardSurface {
            InfoLine("FamX wallet balance", "\u20B9 99,99,999")
            Spacer(Modifier.height(10.dp))
            InfoLine("Limit", "Unlimited")
            Spacer(Modifier.height(10.dp))
            InfoLine("Payments made", txnCount.toString())
            Spacer(Modifier.height(10.dp))
            InfoLine("FamCoins", "245")
            Spacer(Modifier.height(10.dp))
            InfoLine("Version", "1.0 (clone)")
        }

        Spacer(Modifier.height(14.dp))

        FamCardSurface {
            SettingRow(Icons.Rounded.AccountBalance, "Linked bank accounts", "3 added")
            HorizontalDivider(color = GreyLine)
            SettingRow(Icons.Rounded.CreditCard, "FamX card", "Active")
            HorizontalDivider(color = GreyLine)
            SettingRow(Icons.Rounded.Savings, "Keeper \u2022 savings goals", "\u20B95,000")
            HorizontalDivider(color = GreyLine)
            SettingRow(Icons.Rounded.Storefront, "FamStore", "Redeem coins")
            HorizontalDivider(color = GreyLine)
            SettingRow(Icons.Rounded.Palette, "FamStudio", "Themes & sounds")
            HorizontalDivider(color = GreyLine)
            SettingRow(Icons.Rounded.Settings, "Settings", "")
            HorizontalDivider(color = GreyLine)
            SettingRow(Icons.Rounded.HelpOutline, "Help & support", "24x7")
        }

        Spacer(Modifier.height(24.dp))
    }
}

@Composable
private fun SettingRow(icon: ImageVector, title: String, trailing: String) {
    Row(Modifier.fillMaxWidth().padding(vertical = 12.dp), verticalAlignment = Alignment.CenterVertically) {
        Icon(icon, contentDescription = null, tint = Ink, modifier = Modifier.size(21.dp))
        Spacer(Modifier.width(14.dp))
        Text(title, color = Ink, fontSize = 14.sp, modifier = Modifier.weight(1f))
        Text(trailing, color = Grey, fontSize = 12.sp)
    }
}

@Composable
private fun InfoLine(label: String, value: String) {
    Row(Modifier.fillMaxWidth()) {
        Text(label, color = Grey, fontSize = 13.sp)
        Spacer(Modifier.weight(1f))
        Text(value, color = Ink, fontSize = 13.sp, fontWeight = FontWeight.SemiBold)
    }
}
