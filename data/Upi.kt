package com.venice.fakepay.data

import android.net.Uri

data class UpiDetails(
    val vpa: String,
    val name: String,
    val presetAmount: String?,
    val note: String?,
    val currency: String
)

object UpiParser {

    private val vpaRegex = Regex("^[a-zA-Z0-9._\\-]{2,}@[a-zA-Z]{2,}$")

    fun parse(raw: String): UpiDetails? {
        val text = raw.trim()
        if (text.isEmpty()) return null

        if (text.startsWith("upi://", ignoreCase = true)) {
            val uri = runCatching { Uri.parse(text) }.getOrNull() ?: return null
            val vpa = uri.getQueryParameter("pa")
                ?: uri.getQueryParameter("vpa")
                ?: return null
            if (vpa.isBlank()) return null

            val name = uri.getQueryParameter("pn")
                ?: uri.getQueryParameter("name")
                ?: vpa.substringBefore('@').replace('.', ' ')

            return UpiDetails(
                vpa = vpa,
                name = name,
                presetAmount = uri.getQueryParameter("am"),
                note = uri.getQueryParameter("tn"),
                currency = uri.getQueryParameter("cu") ?: "INR"
            )
        }

        if (vpaRegex.matches(text)) {
            return UpiDetails(
                vpa = text,
                name = text.substringBefore('@').replace('.', ' '),
                presetAmount = null,
                note = null,
                currency = "INR"
            )
        }
        return null
    }

    fun fromVpa(vpa: String): UpiDetails? = parse(vpa)
}
