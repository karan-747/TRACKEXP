package com.example.trackexp

import android.net.ipsec.ike.IkeTrafficSelector
import androidx.lifecycle.LiveData
import com.github.mikephil.charting.data.PieEntry
import com.google.android.gms.auth.api.signin.GoogleSignInAccount

interface RepositoryInterface {

    fun getUserName():String

    fun getUserIncome():LiveData<Int>

    fun getUserExpense():LiveData<Int>

    fun getUserBalance():LiveData<Int>

    fun getUserAllRecords():LiveData<ArrayList<TransactionRecord>>

    fun getIsRecordAvailable ():LiveData<Boolean>

    fun getMonthYear():String
    fun getuserLoggedInStatus():Boolean

    fun signOutUser():Unit
    suspend fun loginUser(emailId : String , password:String ):Pair<Boolean,String>
    suspend fun signUpUser(emailId : String , password:String ):Pair<Boolean,String>
    suspend fun signUpWithGoogle(account: GoogleSignInAccount):Pair<Boolean,String>
    suspend fun loginWithGoogle(account: GoogleSignInAccount):Pair<Boolean,String>

    fun getTheDataListForAnalysis(): LiveData<ArrayList<ArrayList<PieEntry>>>
    suspend fun addTransactionRecord(transactionRecord: TransactionRecord):Boolean
    suspend fun deleteTransactionRecord(oldRecord: TransactionRecord):Boolean
    suspend fun updateTransactionRecord (oldRecord: TransactionRecord, updateMap: Map<String, Any>):Boolean

    suspend fun  deletePreviousMonthRecord():Unit
    fun checkLoginStatus( navigateToHome : ()->Unit ):Unit




}