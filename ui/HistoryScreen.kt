package com.venice.fakepay.ui

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.venice.fakepay.data.Txn
import com.venice.fakepay.ui.theme.Grey
import com.venice.fakepay.ui.theme.Ink

@Composable
fun HistoryScreen(transactions: List<Txn>) {
    LazyColumn(
        Modifier.fillMaxSize().padding(horizontal = 16.dp),
        contentPadding = PaddingValues(bottom = 30.dp)
    ) {
        item {
            Text("${transactions.size} payments", color = Grey, fontSize = 13.sp)
            Spacer(Modifier.height(10.dp))
        }
        if (transactions.isEmpty()) {
            item {
                FamCardSurface { Text("No transactions yet", color = Grey) }
            }
        } else {
            items(transactions, key = { it.id }) { txn ->
                FamCardSurface(modifier = Modifier.padding(bottom = 10.dp)) {
                    TxnRow(txn, showDivider = false)
                    Spacer(Modifier.height(6.dp))
                    Text(
                        "UTR: ${txn.utr}  \u2022  ${txn.id}",
                        color = Grey,
                        fontSize = 10.sp
                    )
                }
            }
        }
    }
}
