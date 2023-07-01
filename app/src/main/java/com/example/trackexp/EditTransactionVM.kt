package com.example.trackexp

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.async

class EditTransactionVM:ViewModel() {

    private var repositoryRef:Repository? =null
    init {
        repositoryRef = Repository.getRepositoryInstance()
    }

    suspend fun updateTransactionRecord(oldRecord: TransactionRecord, updateMap: Map<String, Any>):Boolean{
       return repositoryRef?.updateTransactionRecord(oldRecord, updateMap)!!
    }

    suspend fun deleteTransactionRecord(oldRecord: TransactionRecord):Boolean{
        return repositoryRef?.deleteTransactionRecord(oldRecord)!!
    }





}