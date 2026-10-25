package com.venice.fakepay.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.venice.fakepay.data.Txn
import com.venice.fakepay.data.formatDay
import com.venice.fakepay.data.formatInr
import com.venice.fakepay.ui.theme.*

/** Emoji avatar inside a white circle with the signature yellow ring. */
@Composable
fun EmojiAvatar(emoji: String, size: Int = 46, ringed: Boolean = true) {
    Box(
        Modifier
            .size((size + 6).dp)
            .clip(CircleShape)
            .background(if (ringed) Yellow else Color.Transparent)
            .padding(3.dp)
            .clip(CircleShape)
            .background(White)
            .then(if (ringed) Modifier.border(2.dp, White, CircleShape) else Modifier),
        contentAlignment = Alignment.Center
    ) {
        Text(emoji, fontSize = (size * 0.5f).sp)
    }
}

/** Square-ish avatar with a letter, used in lists. */
@Composable
fun LetterAvatar(name: String, size: Int = 44, tint: Color = YellowSoft, fg: Color = Black) {
    val letter = name.trim().firstOrNull()?.uppercaseChar() ?: '?'
    Box(
        Modifier.size(size.dp).clip(RoundedCornerShape(size / 2.6f)).background(tint),
        contentAlignment = Alignment.Center
    ) {
        Text(letter.toString(), color = fg, fontWeight = FontWeight.Bold, fontSize = (size * 0.4f).sp)
    }
}

@Composable
fun Pill(
    text: String,
    bg: Color = Yellow,
    fg: Color = Black,
    fontSize: Int = 11
) {
    Box(
        Modifier
            .clip(RoundedCornerShape(50))
            .background(bg)
            .padding(horizontal = 10.dp, vertical = 4.dp)
    ) {
        Text(text, color = fg, fontSize = fontSize.sp, fontWeight = FontWeight.Bold)
    }
}

/** White rounded card used everywhere in FamPay. */
@Composable
fun FamCardSurface(
    modifier: Modifier = Modifier,
    padding: Int = 16,
    onClick: (() -> Unit)? = null,
    content: @Composable ColumnScope.() -> Unit
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(20.dp))
            .background(White)
            .then(if (onClick != null) Modifier.clickable { onClick() } else Modifier)
            .padding(padding.dp),
        content = content
    )
}

/** Row used in "For you" section: icon tile + title/subtitle + chevron. */
@Composable
fun ForYouRow(
    icon: ImageVector,
    iconBg: Color,
    iconTint: Color,
    title: String,
    subtitle: String,
    onClick: () -> Unit
) {
    FamCardSurface(onClick = onClick) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Box(
                Modifier.size(44.dp).clip(RoundedCornerShape(14.dp)).background(iconBg),
                contentAlignment = Alignment.Center
            ) {
                androidx.compose.material3.Icon(icon, contentDescription = null, tint = iconTint)
            }
            Spacer(Modifier.width(14.dp))
            Column(Modifier.weight(1f)) {
                Text(title, fontWeight = FontWeight.Bold, fontSize = 15.sp, color = Ink)
                Text(subtitle, fontSize = 12.sp, color = Grey)
            }
            Text("\u203A", fontSize = 22.sp, color = Grey, fontWeight = FontWeight.Bold)
        }
    }
}

@Composable
fun TxnRow(txn: Txn, showDivider: Boolean = true) {
    val outgoing = txn.direction == "PAID"
    Column {
        Row(
            Modifier.fillMaxWidth().padding(vertical = 12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            LetterAvatar(txn.name, 44)
            Spacer(Modifier.width(12.dp))
            Column(Modifier.weight(1f)) {
                Text(txn.name, fontWeight = FontWeight.Bold, fontSize = 15.sp, color = Ink, maxLines = 1)
                Text(
                    txn.note.ifBlank { txn.vpa },
                    fontSize = 12.sp,
                    color = Grey,
                    maxLines = 1
                )
                Text(formatDay(txn.time), fontSize = 11.sp, color = Grey)
            }
            Text(
                (if (outgoing) "-" else "+") + formatInr(txn.amount),
                fontWeight = FontWeight.Bold,
                fontSize = 15.sp,
                color = if (outgoing) Ink else Green
            )
        }
        if (showDivider) HorizontalDivider(color = GreyLine, thickness = 1.dp)
    }
}

@Composable
fun SectionHeader(title: String, trailing: (@Composable () -> Unit)? = null) {
    Row(
        Modifier.fillMaxWidth().padding(vertical = 12.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(title, fontWeight = FontWeight.ExtraBold, fontSize = 17.sp, color = Ink, modifier = Modifier.weight(1f))
        trailing?.invoke()
    }
}

/** Small round back button on black screens. */
@Composable
fun DarkCircleButton(icon: ImageVector, onClick: () -> Unit) {
    Box(
        Modifier
            .size(42.dp)
            .clip(CircleShape)
            .background(White.copy(alpha = 0.12f))
            .clickable { onClick() },
        contentAlignment = Alignment.Center
    ) {
        androidx.compose.material3.Icon(icon, contentDescription = null, tint = White)
    }
}
