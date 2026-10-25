package com.venice.fakepay.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.venice.fakepay.AppViewModel
import com.venice.fakepay.data.*
import com.venice.fakepay.ui.theme.*

sealed interface Screen {
    data object Splash : Screen
    data object Home : Screen
    data object Family : Screen
    data object History : Screen
    data object Profile : Screen
    data object FamCard : Screen
    data object FamCoins : Screen
    data object Scan : Screen
    data class Pay(val details: UpiDetails) : Screen
    data class Success(val txn: Txn) : Screen
}

@Composable
fun FamPayApp() {
    val vm: AppViewModel = viewModel()
    val transactions by vm.transactions.collectAsState()

    var screen by remember { mutableStateOf<Screen>(Screen.Splash) }
    var coins by remember { mutableIntStateOf(245) }
    var showPaySheet by remember { mutableStateOf(false) }
    var showManual by remember { mutableStateOf(false) }

    LaunchedEffect(Unit) { vm.seedIfEmpty() }

    FamPayTheme {
        when (val current = screen) {

            is Screen.Splash -> SplashScreen(onDone = { screen = Screen.Home })

            is Screen.Home, is Screen.Family -> {
                Scaffold(
                    containerColor = Bg,
                    bottomBar = {
                        FamBottomBar(
                            current = current,
                            onHome = { screen = Screen.Home },
                            onFamily = { screen = Screen.Family },
                            onPay = { showPaySheet = true }
                        )
                    }
                ) { padding ->
                    Box(Modifier.padding(padding)) {
                        when (current) {
                            is Screen.Home -> HomeScreen(
                                transactions = transactions,
                                coins = coins,
                                onScan = { screen = Screen.Scan },
                                onManual = { showManual = true },
                                onHistory = { screen = Screen.History },
                                onFamCard = { screen = Screen.FamCard },
                                onFamCoins = { screen = Screen.FamCoins },
                                onProfile = { screen = Screen.Profile }
                            )
                            else -> FamilyScreen(transactions)
                        }
                    }
                }
            }

            is Screen.History -> SimpleTopScaffold("Transactions", { screen = Screen.Home }) {
                HistoryScreen(transactions)
            }

            is Screen.FamCard -> SimpleTopScaffold("FamX Card", { screen = Screen.Home }) {
                FamCardScreen(transactions)
            }

            is Screen.FamCoins -> SimpleTopScaffold("FamCoins", { screen = Screen.Home }) {
                FamCoinsScreen(coins) { coins += 25 }
            }

            is Screen.Profile -> SimpleTopScaffold("Profile", { screen = Screen.Home }) {
                ProfileScreen(transactions.size)
            }

            is Screen.Scan -> ScannerScreen(
                onBack = { screen = Screen.Home },
                onDetails = { screen = Screen.Pay(it) }
            )

            is Screen.Pay -> PayScreen(
                details = current.details,
                onBack = { screen = Screen.Scan },
                onPaid = { amount, note ->
                    val txn = Txn(
                        id = "FMPIB" + newRefId(),
                        utr = newUtr(),
                        name = current.details.name,
                        vpa = current.details.vpa,
                        amount = amount,
                        note = note,
                        time = System.currentTimeMillis(),
                        direction = "PAID"
                    )
                    vm.save(txn)
                    screen = Screen.Success(txn)
                }
            )

            is Screen.Success -> SuccessScreen(
                txn = current.txn,
                onDone = { screen = Screen.Home }
            )
        }

        if (showPaySheet) {
            PayOptionsSheet(
                onDismiss = { showPaySheet = false },
                onScan = { showPaySheet = false; screen = Screen.Scan },
                onManual = { showPaySheet = false; showManual = true }
            )
        }

        if (showManual) {
            ManualUpiDialog(
                onDismiss = { showManual = false },
                onSubmit = { showManual = false; screen = Screen.Pay(it) }
            )
        }
    }
}

/** Bottom bar with the signature floating yellow rupee button. */
@Composable
private fun FamBottomBar(
    current: Screen,
    onHome: () -> Unit,
    onFamily: () -> Unit,
    onPay: () -> Unit
) {
    Box(Modifier.fillMaxWidth().background(White)) {
        Row(
            Modifier
                .fillMaxWidth()
                .padding(top = 8.dp, bottom = 8.dp),
            horizontalArrangement = Arrangement.SpaceEvenly,
            verticalAlignment = Alignment.CenterVertically
        ) {
            BottomTab("Home", Icons.Rounded.Home, current is Screen.Home, onHome)
            Spacer(Modifier.width(64.dp))
            BottomTab("Family", Icons.Rounded.FavoriteBorder, current is Screen.Family, onFamily)
        }

        Box(
            Modifier
                .align(Alignment.TopCenter)
                .offset(y = (-20).dp)
                .size(62.dp)
                .clip(CircleShape)
                .background(Yellow)
                .clickable2(onPay),
            contentAlignment = Alignment.Center
        ) {
            Text("\u20B9", fontSize = 30.sp, fontWeight = FontWeight.Black, color = Black)
        }
    }
}

@Composable
private fun BottomTab(
    label: String,
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    selected: Boolean,
    onClick: () -> Unit
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .clip(RoundedCornerShape(16.dp))
            .clickable2(onClick)
            .padding(horizontal = 20.dp, vertical = 6.dp)
    ) {
        Icon(
            icon,
            contentDescription = label,
            tint = if (selected) Black else Grey,
            modifier = Modifier.size(24.dp)
        )
        Spacer(Modifier.height(2.dp))
        Text(
            label,
            fontSize = 11.sp,
            fontWeight = if (selected) FontWeight.Bold else FontWeight.Medium,
            color = if (selected) Black else Grey
        )
    }
}

@Composable
fun SimpleTopScaffold(
    title: String,
    onBack: () -> Unit,
    content: @Composable () -> Unit
) {
    Scaffold(
        containerColor = Bg,
        topBar = {
            Row(
                Modifier
                    .fillMaxWidth()
                    .background(Bg)
                    .statusBarsPadding()
                    .padding(horizontal = 16.dp, vertical = 10.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                IconButton(onClick = onBack) {
                    Icon(Icons.AutoMirrored.Rounded.ArrowBack, contentDescription = "Back", tint = Ink)
                }
                Text(title, fontWeight = FontWeight.ExtraBold, fontSize = 19.sp, color = Ink)
            }
        }
    ) { padding ->
        Box(Modifier.padding(padding)) { content() }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun PayOptionsSheet(
    onDismiss: () -> Unit,
    onScan: () -> Unit,
    onManual: () -> Unit
) {
    ModalBottomSheet(
        onDismissRequest = onDismiss,
        containerColor = White,
        shape = RoundedCornerShape(topStart = 28.dp, topEnd = 28.dp)
    ) {
        Column(Modifier.padding(horizontal = 20.dp).padding(bottom = 32.dp)) {
            Text("Pay", fontWeight = FontWeight.ExtraBold, fontSize = 22.sp, color = Ink)
            Spacer(Modifier.height(16.dp))

            SheetOption(Icons.Rounded.QrCodeScanner, "Scan QR code", "Pay at any shop", onScan)
            Spacer(Modifier.height(10.dp))
            SheetOption(Icons.Rounded.AlternateEmail, "Enter UPI ID", "name@bank", onManual)
            Spacer(Modifier.height(10.dp))
            SheetOption(Icons.Rounded.Person, "Pay to contact", "From your phone book", onManual)
        }
    }
}

@Composable
private fun SheetOption(
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    title: String,
    subtitle: String,
    onClick: () -> Unit
) {
    FamCardSurface(padding = 14, onClick = onClick) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Box(
                Modifier.size(46.dp).clip(RoundedCornerShape(14.dp)).background(YellowSoft),
                contentAlignment = Alignment.Center
            ) { Icon(icon, contentDescription = null, tint = Black) }
            Spacer(Modifier.width(14.dp))
            Column {
                Text(title, fontWeight = FontWeight.Bold, fontSize = 15.sp, color = Ink)
                Text(subtitle, fontSize = 12.sp, color = Grey)
            }
        }
    }
}

@Composable
private fun ManualUpiDialog(
    onDismiss: () -> Unit,
    onSubmit: (UpiDetails) -> Unit
) {
    var value by remember { mutableStateOf("") }
    val parsed = UpiParser.fromVpa(value)

    AlertDialog(
        onDismissRequest = onDismiss,
        containerColor = White,
        shape = RoundedCornerShape(24.dp),
        title = { Text("Pay to UPI ID", fontWeight = FontWeight.ExtraBold) },
        text = {
            Column {
                OutlinedTextField(
                    value = value,
                    onValueChange = { value = it.trim() },
                    placeholder = { Text("name@bank") },
                    singleLine = true,
                    shape = RoundedCornerShape(16.dp),
                    modifier = Modifier.fillMaxWidth()
                )
                Spacer(Modifier.height(8.dp))
                Text(
                    text = when {
                        value.isBlank() -> "Example: 9876543210@ybl"
                        parsed == null -> "Invalid UPI ID"
                        else -> "Payee: ${parsed.name}"
                    },
                    color = if (value.isNotBlank() && parsed == null) Red else Grey,
                    fontSize = 12.sp
                )
            }
        },
        confirmButton = {
            Button(
                onClick = { parsed?.let(onSubmit) },
                enabled = parsed != null,
                colors = ButtonDefaults.buttonColors(containerColor = Yellow, contentColor = Black)
            ) { Text("Continue", fontWeight = FontWeight.Bold) }
        },
        dismissButton = { TextButton(onClick = onDismiss) { Text("Cancel", color = Grey) } }
    )
}

fun Modifier.clickable2(onClick: () -> Unit): Modifier =
    this.then(androidx.compose.foundation.clickable(onClick = onClick))
