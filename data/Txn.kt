package com.venice.fakepay.data

import kotlinx.serialization.Serializable
import java.text.NumberFormat
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@Serializable
data class Txn(
    val id: String,
    val utr: String,
    val name: String,
    val vpa: String,
    val amount: Double,
    val note: String,
    val time: Long,
    val direction: String,      // PAID | RECEIVED
    val via: String = "FamPay UPI"
)

fun newRefId(): String = buildString { repeat(12) { append((0..9).random()) } }
fun newUtr(): String = buildString { repeat(11) { append((0..9).random()) } }

private fun nf(decimals: Int): NumberFormat =
    NumberFormat.getNumberInstance(Locale("en", "IN")).apply {
        minimumFractionDigits = decimals
        maximumFractionDigits = decimals
    }

fun formatInr(value: Double): String = "\u20B9" + nf(2).format(value)
fun formatInrShort(value: Double): String = "\u20B9" + nf(0).format(value)

fun formatDateTime(millis: Long): String =
    SimpleDateFormat("dd MMM yyyy 'at' hh:mm a", Locale.getDefault()).format(Date(millis))

fun formatDay(millis: Long): String =
    SimpleDateFormat("dd MMM, hh:mm a", Locale.getDefault()).format(Date(millis))
