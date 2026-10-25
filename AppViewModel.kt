package com.venice.fakepay

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.venice.fakepay.data.Txn
import com.venice.fakepay.data.TxnRepository
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class AppViewModel(app: Application) : AndroidViewModel(app) {

    private val repo = TxnRepository(app)

    val transactions: StateFlow<List<Txn>> =
        repo.transactions.stateIn(viewModelScope, SharingStarted.Eagerly, emptyList())

    fun save(txn: Txn) = viewModelScope.launch { repo.add(txn) }
    fun seedIfEmpty() = viewModelScope.launch { repo.seedIfEmpty() }
}
