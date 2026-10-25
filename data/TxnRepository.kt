package com.venice.fakepay.data

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import kotlinx.serialization.json.Json

private val Context.dataStore: DataStore<Preferences> by
    preferencesDataStore(name = "fampay_store")

class TxnRepository(private val context: Context) {

    private val key = stringPreferencesKey("transactions")
    private val json = Json { ignoreUnknownKeys = true; encodeDefaults = true }

    private fun decode(raw: String?): List<Txn> =
        if (raw.isNullOrBlank()) emptyList()
        else runCatching { json.decodeFromString<List<Txn>>(raw) }.getOrDefault(emptyList())

    val transactions: Flow<List<Txn>> = context.dataStore.data.map { decode(it[key]) }

    suspend fun add(txn: Txn) {
        context.dataStore.edit { prefs ->
            prefs[key] = json.encodeToString(listOf(txn) + decode(prefs[key]))
        }
    }

    suspend fun seedIfEmpty() {
        val prefs = context.dataStore.data.first()
        if (prefs[key] != null) return

        val now = System.currentTimeMillis()
        val seeds = listOf(
            Txn(newRefId(), newUtr(), "Salke's", "salke@fam", 250.0, "Chai and samosa", now - 2_400_000L, "PAID"),
            Txn(newRefId(), newUtr(), "FamCoins Reward", "rewards@fam", 25.0, "Scratch card", now - 7_200_000L, "RECEIVED"),
            Txn(newRefId(), newUtr(), "Aditi", "aditi.k@ybl", 480.0, "Cold coffee", now - 96_000_000L, "PAID"),
            Txn(newRefId(), newUtr(), "Anjor", "anjor@okhdfcbank", 1200.0, "Trip split", now - 200_000_000L, "RECEIVED"),
            Txn(newRefId(), newUtr(), "Blinkit", "blinkit@icici", 349.0, "Groceries", now - 300_000_000L, "PAID")
        )
        context.dataStore.edit { it[key] = json.encodeToString(seeds) }
    }
}
