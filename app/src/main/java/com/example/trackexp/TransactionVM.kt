package com.example.trackexp

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class TransactionVM :ViewModel() {
    private var repositoryRef :Repository? =null
    init{
        repositoryRef = Repository.getRepositoryInstance()
    }

    suspend fun addTransaction(transaction: TransactionRecord):Boolean
    {
       return repositoryRef?.addTransactionRecord(transaction)!!
    }
}