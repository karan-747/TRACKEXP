package com.example.trackexp

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel

class TransactionHistoryVM:ViewModel() {

    private var repositoryRef:Repository? = null
    init {
        repositoryRef= Repository.getRepositoryInstance()
    }

    fun getUserAllRecords():LiveData<ArrayList<TransactionRecord>> = repositoryRef?.getUserAllRecords()!!
}