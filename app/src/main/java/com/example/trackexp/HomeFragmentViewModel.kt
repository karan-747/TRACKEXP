package com.example.trackexp

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import java.text.DateFormat
import java.util.Calendar

class HomeFragmentViewModel():ViewModel() {

    private val TAG = "HOMEFRAGMENTVIEWMODEL"
    private var repositoryRef : Repository? = null
    private val calender = Calendar.getInstance()

    init{
         repositoryRef = Repository.getRepositoryInstance()

    }

    fun getUserIncome():LiveData<Int> = repositoryRef?.getUserIncome()!!

    fun getUserExpense():LiveData<Int> = repositoryRef?.getUserExpense()!!

    fun getUserBalance():LiveData<Int> = repositoryRef?.getUserBalance()!!

    fun getCurrentDate(): String {
        return DateFormat.getDateInstance(DateFormat.FULL).format(calender.time).toString()
    }

    fun getUserName():String = repositoryRef?.getUserName()!!

    fun checkIsRecordAvailable() :LiveData<Boolean> = repositoryRef?.getIsRecordAvailable()!!

    fun getTheRecordsData(): LiveData<ArrayList<TransactionRecord>> = repositoryRef?.getUserAllRecords()!!

    fun signOutUser()=repositoryRef?.signOutUser()

    private fun showLog(message:String){
        Log.d(TAG,message)
    }

}