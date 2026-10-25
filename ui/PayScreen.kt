package com.venice.fakepay.ui

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Close
import androidx.compose.material.icons.rounded.Verified
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.venice.fakepay.data.UpiDetails
import com.venice.fakepay.ui.theme.*

@Composable
fun PayScreen(
    details: UpiDetails,
    onBack: () -> Unit,
    onPaid: (amount: Double, note: String) -> Unit
) {
    BackHandler { onBack() }

    var amount by remember { mutableStateOf(details.presetAmount ?: "") }
    var note by remember { mutableStateOf(details.note ?: "") }
    val parsed = amount.toDoubleOrNull()
    val valid = parsed != null && parsed > 0

    Box(Modifier.fillMaxSize().background(Color(0xFF101012))) {

        Column(
            Modifier
                .fillMaxSize()
                .statusBarsPadding()
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 20.dp)
        ) {
            Spacer(Modifier.height(10.dp))

            Row(verticalAlignment = Alignment.CenterVertically) {
                DarkCircleButton(Icons.Rounded.Close, onBack)
                Spacer(Modifier.weight(1f))
                Text("Paying", color = White.copy(alpha = 0.6f), fontSize = 13.sp)
            }

            Spacer(Modifier.height(28.dp))

            // Payee
            Row(verticalAlignment = Alignment.CenterVertically) {
                LetterAvatar(details.name, 54, tint = Yellow, fg = Black)
                Spacer(Modifier.width(14.dp))
                Column(Modifier.weight(1f)) {
                    Text(details.name, color = White, fontWeight = FontWeight.ExtraBold, fontSize = 18.sp)
                    Text(details.vpa, color = White.copy(alpha = 0.55f), fontSize = 13.sp)
                }
                Icon(Icons.Rounded.Verified, contentDescription = null, tint = Yellow, modifier = Modifier.size(20.dp))
            }

            Spacer(Modifier.height(32.dp))

            // Amount — big gold, FamPay style
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text("\u20B9", color = Yellow, fontSize = 44.sp, fontWeight = FontWeight.Black)
                Spacer(Modifier.width(6.dp))
                BasicTextField2(
                    value = amount,
                    onChange = { input ->
                        if (input.isEmpty() || input.matches(Regex("^\\d{0,9}(\\.\\d{0,2})?$"))) amount = input
                    }
                )
            }
            Spacer(Modifier.height(2.dp))
            Text("Enter amount", color = White.copy(alpha = 0.45f), fontSize = 12.sp)

            Spacer(Modifier.height(20.dp))

            Row(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                listOf(100, 250, 500, 1000).forEach { preset ->
                    Box(
                        Modifier
                            .clip(RoundedCornerShape(50))
                            .background(if (amount == preset.toString()) Yellow else White.copy(alpha = 0.1f))
                            .clickable { amount = preset.toString() }
                            .padding(horizontal = 16.dp, vertical = 9.dp)
                    ) {
                        Text(
                            "\u20B9$preset",
                            color = if (amount == preset.toString()) Black else White,
                            fontWeight = FontWeight.Bold,
                            fontSize = 13.sp
                        )
                    }
                }
            }

            Spacer(Modifier.height(26.dp))

            // Note on dark
            OutlinedTextField(
                value = note,
                onValueChange = { note = it },
                modifier = Modifier.fillMaxWidth(),
                label = { Text("Add a note", color = White.copy(alpha = 0.6f)) },
                singleLine = true,
                shape = RoundedCornerShape(16.dp),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedTextColor = White,
                    unfocusedTextColor = White,
                    focusedBorderColor = Yellow,
                    unfocusedBorderColor = White.copy(alpha = 0.25f),
                    cursorColor = Yellow
                )
            )

            Spacer(Modifier.height(24.dp))

            Column(
                Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(18.dp))
                    .background(White.copy(alpha = 0.06f))
                    .padding(16.dp)
            ) {
                InfoLine("Paying to", details.name)
                Spacer(Modifier.height(10.dp))
                InfoLine("UPI ID", details.vpa)
                Spacer(Modifier.height(10.dp))
                InfoLine("Wallet balance", "\u20B9 99,99,999 (unchanged)")
                Spacer(Modifier.height(10.dp))
                InfoLine("Mode", "Fake \u2022 no real money")
            }

            Spacer(Modifier.height(120.dp))
        }

        // Bottom sheet with Pay button
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
                Text("FamX wallet", color = Grey, fontSize = 12.sp)
                Spacer(Modifier.weight(1f))
                Text("\u20B9 99,99,999", color = Ink, fontWeight = FontWeight.Bold, fontSize = 12.sp)
            }
            Spacer(Modifier.height(12.dp))
            Button(
                onClick = { if (valid) onPaid(parsed!!, note) },
                enabled = valid,
                modifier = Modifier.fillMaxWidth().height(56.dp),
                shape = RoundedCornerShape(18.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Yellow,
                    contentColor = Black,
                    disabledContainerColor = YellowSoft,
                    disabledContentColor = Grey
                )
            ) {
                Text(
                    if (valid) "Pay \u20B9${"%.2f".format(parsed)}" else "Enter amount",
                    fontSize = 17.sp,
                    fontWeight = FontWeight.ExtraBold
                )
            }
        }
    }
}

@Composable
private fun InfoLine(label: String, value: String) {
    Row(Modifier.fillMaxWidth()) {
        Text(label, color = White.copy(alpha = 0.5f), fontSize = 13.sp)
        Spacer(Modifier.weight(1f))
        Text(value, color = White, fontSize = 13.sp, fontWeight = FontWeight.Medium)
    }
}

@Composable
private fun BasicTextField2(value: String, onChange: (String) -> Unit) {
    BasicTextField(
        value = value,
        onValueChange = onChange,
        textStyle = TextStyle(color = Yellow, fontSize = 44.sp, fontWeight = FontWeight.Black),
        singleLine = true,
        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
        cursorBrush = androidx.compose.ui.graphics.SolidColor(Yellow),
        decorationBox = { inner ->
            if (value.isEmpty()) {
                Text("0", color = Yellow.copy(alpha = 0.35f), fontSize = 44.sp, fontWeight = FontWeight.Black)
            }
            inner()
        }
    )
}
