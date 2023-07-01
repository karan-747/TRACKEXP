package com.example.trackexp

import androidx.datastore.dataStore
import androidx.lifecycle.LiveData
import com.github.mikephil.charting.data.PieEntry
import com.google.android.gms.auth.api.signin.GoogleSignInAccount

class Repository :RepositoryInterface{
     private var dataSource :FireBaseDataSource? =null

    companion object{
        private var INSTANCE:Repository? =null
        fun getRepositoryInstance() :Repository{
            if(INSTANCE == null){
                synchronized(this){
                    INSTANCE = Repository()
                }
            }
            return INSTANCE!!
        }
    }

    init {
          dataSource = FireBaseDataSource.getDataSourceInstance()
    }
    override fun getUserName(): String  =  dataSource?.getUserName()!!
    override fun getUserIncome(): LiveData<Int> = dataSource?.getUserIncome()!!
    override fun getUserExpense(): LiveData<Int> = dataSource?.getUserExpense()!!
    override fun getUserBalance(): LiveData<Int> = dataSource?.getUserBalance()!!
    override fun getUserAllRecords(): LiveData<ArrayList<TransactionRecord>> =dataSource?.getUserRecordsData()!!
    override fun getIsRecordAvailable(): LiveData<Boolean> = dataSource?.checkIsRecordAvailable()!!
    override fun getMonthYear(): String {
        return dataSource?.getMonthYear()!!
    }

    override fun getuserLoggedInStatus(): Boolean {
        return dataSource?.getLoggedInStatus()!!
    }

    override fun signOutUser() {
         dataSource?.signOutUser()
    }

    override fun getTheDataListForAnalysis(): LiveData<ArrayList<ArrayList<PieEntry>>> {
        return  dataSource?.getTheDataListForAnalysis()!!
    }

    override suspend fun addTransactionRecord(transactionRecord: TransactionRecord): Boolean {
         return dataSource?.addTransactionRecord(transactionRecord)!!
     }

    override suspend fun deleteTransactionRecord(oldRecord: TransactionRecord): Boolean {
         return  dataSource?.deleteTransactionRecord(oldRecord)!!
     }

    override suspend fun updateTransactionRecord(oldRecord: TransactionRecord, updateMap: Map<String, Any>): Boolean {
         return dataSource?.updateTransactionRecord(oldRecord,updateMap)!!
     }

    override suspend fun loginUser(emailId: String, password: String): Pair<Boolean,String> {
        return dataSource?.loginUser(emailId,password)!!
    }

    override suspend fun signUpUser(emailId: String, password: String): Pair<Boolean, String> {
        return dataSource?.signUpUser(emailId,password)!!
    }

    override suspend fun loginWithGoogle(account: GoogleSignInAccount): Pair<Boolean, String> {
       return dataSource?.loginWithGoogle(account)!!
    }

    override suspend fun signUpWithGoogle(account: GoogleSignInAccount): Pair<Boolean, String> {
        return dataSource?.signUpWithGoogle(account)!!
    }



 }