package com.example.trackexp

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.github.mikephil.charting.data.PieEntry
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.firestore.Query
import com.google.firebase.firestore.SetOptions
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.firestore.ktx.toObject
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.tasks.await
import java.lang.Exception
import java.text.DateFormat
import java.text.SimpleDateFormat
import java.util.Calendar

class FireBaseDataSource {




    companion object{
        private var INSTANCE :FireBaseDataSource? = null
        fun getDataSourceInstance():FireBaseDataSource{

            if(INSTANCE == null){
                synchronized(this){
                    INSTANCE = FireBaseDataSource()
                }
            }
            return INSTANCE!!
        }
    }




    private var firebaseAuth = FirebaseAuth.getInstance()
    private var user = firebaseAuth.currentUser
    private  var uid = user?.uid.toString()
    private var userName = user?.displayName.toString()
    private var refPath =  "TRACKEXP/USERS/USERRECORDS/${user?.uid.toString()}/RECORDS"
    private var recordRef = Firebase.firestore.collection(refPath)

    //is records available
    private var isRecordAvailable = MutableLiveData<Boolean>(false)

    //list of records
    private val recordList = ArrayList<TransactionRecord>()


    //income expense balance
    private  var transactionRecords = MutableLiveData<ArrayList<TransactionRecord>>()
    private var income = 0
    private var expense =0
    private var balance =0
    private var incomeData = MutableLiveData<Int>(0)
    private var expenseData = MutableLiveData<Int>(0)
    private var balanceData = MutableLiveData<Int>(0)

    //calender for the current month and year
    private val calender = Calendar.getInstance()
    private val currentMonth = calender.get(Calendar.MONTH)+1
    private val currentYear = calender.get(Calendar.YEAR)
    private val currentDay = calender.get(Calendar.DAY_OF_MONTH)


    //for analysis

    private val dataListLiveData = MutableLiveData<ArrayList<ArrayList<PieEntry> > >()
    private val dataList:ArrayList<ArrayList<PieEntry>> = ArrayList(ArrayList())


    private var incomeOnline=0
    private var incomeCash=0

    private var incomeWages=0
    private var incomeSalary=0
    private var incomePocketMoney=0
    private var incomeGovernmentPayment=0
    private var incomeGifts=0
    private var incomeCommission=0
    private var incomeInterests = 0
    private var incomeInvestments = 0
    private var incomeOthers = 0


    private var expenseOnline =0
    private var expenseCash =0

    private var expenseEducation=0
    private var expenseFood=0
    private var expenseGrocery=0
    private var expenseHouseRent=0
    private var expenseInternet=0
    private var expenseInvestment=0
    private var expenseShopping = 0
    private var expenseTravel = 0
    private var expenseOthers = 0


    init {
        user?.let {
            getDatabaseRecords()
        }

    }

    //getting a data from the data source -- firebase cloud storage

    private  fun getDatabaseRecords(){

        recordRef.whereEqualTo("month",currentMonth)
            .orderBy("day" , Query.Direction.DESCENDING)
            .whereEqualTo("year",currentYear)
            .addSnapshotListener { snapShot, e ->

                e?.let{
                    //Log.d("FIREBASEDATASOURCE",e.message.toString())
                    return@addSnapshotListener
                }
                snapShot?.let{
                    resetData()
                    refreshData()
                    for(doc in it){
                        var record = doc.toObject<TransactionRecord>()
                        if(record.type == "Income"){
                            income+= record.amount
                        }else{
                            expense += record.amount
                        }

                        when(record.type){
                            "Income"->{
                                when(record.mode){
                                    "Online"->{
                                        incomeOnline += record.amount
                                    }
                                    "Cash"->{
                                        incomeCash += record.amount
                                    }
                                }
                                when(record.category){
                                    "Wages"->{
                                        incomeWages += record.amount
                                    }
                                    "Salary"->{
                                        incomeSalary += record.amount
                                    }
                                    "Interests"->{
                                        incomeInterests += record.amount
                                    }
                                    "Investments"->{
                                        incomeInvestments += record.amount
                                    }
                                    "Commission"->{
                                        incomeCommission += record.amount
                                    }
                                    "Gifts"->{
                                        incomeGifts += record.amount
                                    }
                                    "Pocket Money"->{
                                        incomePocketMoney += record.amount
                                    }

                                    "Government Payments"->{
                                        incomeGovernmentPayment += record.amount
                                    }
                                    "Others" ->{
                                        incomeOthers += record.amount
                                    }

                                }


                            }
                            "Expense"->{
                                when(record.mode){
                                    "Online"->{
                                        expenseOnline += record.amount
                                    }
                                    "Cash"->{
                                        expenseCash += record.amount
                                    }
                                }
                                when(record.category){
                                    "Investment"->{
                                        expenseInvestment += record.amount
                                    }
                                    "Education"->{
                                        expenseEducation += record.amount
                                    }
                                    "Food"->{
                                        expenseFood += record.amount
                                    }
                                    "Grocery"->{
                                        expenseGrocery += record.amount
                                    }
                                    "House Rent"->{
                                        expenseHouseRent += record.amount
                                    }
                                    "Internet"->{
                                        expenseInternet += record.amount
                                    }
                                    "Shopping"->{
                                        expenseShopping += record.amount
                                    }
                                    "Travel"->{
                                        expenseTravel += record.amount
                                    }
                                    "Others"->{
                                        expenseOthers += record.amount
                                    }
                                }
                            }
                        }



                        recordList.add(record)
                    }
                    setTheData()
                    balance =income -expense
                    incomeData.value = income
                    expenseData.value = expense
                    balanceData.value = balance
                    isRecordAvailable.value = recordList.isNotEmpty()
                    transactionRecords.value = recordList
                }
            }
    }
    private fun resetData() {
        income=0
        expense=0
        balance=0
        recordList.clear()
    }

    private fun refreshData() {
        incomeOnline=0
        incomeCash=0
        expenseOnline =0
        expenseCash =0
        expenseEducation=0
        expenseFood=0
        expenseGrocery=0
        expenseHouseRent=0
        expenseInternet=0
        expenseInvestment=0
        expenseShopping = 0
        expenseTravel = 0
        expenseOthers = 0

        incomeWages=0
        incomeOthers=0
        incomeSalary=0

        incomeCommission =0
        incomeGovernmentPayment=0
        incomeInvestments=0

        incomePocketMoney=0
        incomeInterests=0
        incomeGifts=0

        dataList.clear()
    }

    private fun setTheData() {
        val incomeModeList= arrayListOf<PieEntry>(
            PieEntry(incomeOnline.toFloat(),"Online"),
            PieEntry(incomeCash.toFloat(),"Cash")
        )
        val expenseModeList = arrayListOf<PieEntry>(
            PieEntry(expenseOnline.toFloat(),"Online"),
            PieEntry(expenseCash.toFloat(),"Cash")
        )

        val expenseCategoryList = arrayListOf<PieEntry>()
        if(expenseEducation !=0 ){
            expenseCategoryList.add(PieEntry(expenseEducation.toFloat(),"Education"))
        }

        if(expenseFood !=0 ){
            expenseCategoryList.add(PieEntry(expenseFood.toFloat(),"Food"))
        }

        if(expenseGrocery !=0 ){
            expenseCategoryList.add(PieEntry(expenseGrocery.toFloat(),"Grocery"))
        }
        if(expenseHouseRent !=0 ){
            expenseCategoryList.add(PieEntry(expenseHouseRent.toFloat(),"House Rent"))
        }
        if(expenseInternet !=0 ){
            expenseCategoryList.add(PieEntry(expenseInternet.toFloat(),"Internet"))
        }
        if(expenseInvestment !=0 ){
            expenseCategoryList.add(PieEntry(expenseInvestment.toFloat(),"Investment"))
        }
        if(expenseShopping !=0 ){
            expenseCategoryList.add(PieEntry(expenseShopping.toFloat(),"Shopping"))
        }
        if(expenseTravel !=0 ){
            expenseCategoryList.add(PieEntry(expenseTravel.toFloat(),"Travel"))
        }
        if(expenseOthers !=0 ){
            expenseCategoryList.add(PieEntry(expenseOthers.toFloat(),"Others"))
        }


        //Income category

        val incomeCategoryList = arrayListOf<PieEntry>()
        if(incomeWages !=0 ){
            incomeCategoryList.add(PieEntry(incomeWages.toFloat(),"Wages"))
        }

        if(incomeSalary !=0 ){
            incomeCategoryList.add(PieEntry(incomeSalary.toFloat(),"Salary"))
        }

        if(incomeCommission !=0 ){
            incomeCategoryList.add(PieEntry(incomeCommission.toFloat(),"Commission"))
        }
        if(incomeInterests !=0 ){
            incomeCategoryList.add(PieEntry(incomeInterests.toFloat(),"Interests"))
        }
        if(incomeInvestments !=0 ){
            incomeCategoryList.add(PieEntry(incomeInvestments.toFloat(),"Investments"))
        }
        if(incomeGifts !=0 ){
            incomeCategoryList.add(PieEntry(incomeGifts.toFloat(),"Gifts"))
        }
        if(incomePocketMoney !=0 ){
            incomeCategoryList.add(PieEntry(incomePocketMoney.toFloat(),"Pocket Money"))
        }
        if(incomeGovernmentPayment !=0 ){
            incomeCategoryList.add(PieEntry(incomeGovernmentPayment.toFloat(),"Government Payments"))
        }
        if(incomeOthers !=0 ){
            incomeCategoryList.add(PieEntry(incomeOthers.toFloat(),"Others"))
        }

        dataList.add(incomeModeList)
        dataList.add(incomeCategoryList)
        dataList.add(expenseModeList)

        dataList.add(expenseCategoryList)


        dataListLiveData.value = dataList

    }



    //data getting functions

    fun getUserIncome(): LiveData<Int> =incomeData

    fun getUserExpense(): LiveData<Int> = expenseData

    fun getUserBalance(): LiveData<Int> = balanceData

    fun getCurrentDate():String{
        val date = DateFormat.getDateInstance(DateFormat.FULL).format(calender.time).toString()
        return date
    }

    fun getUserName():String{
        user?.displayName?.let {
            return it
        }
        return "User"
    }

    fun checkIsRecordAvailable() : LiveData<Boolean> =isRecordAvailable

    fun getUserRecordsData() = transactionRecords

    fun signOutUser():Unit {
        user = null
        firebaseAuth.signOut()
    }
    fun getLoggedInStatus():Boolean{
        return user!=null
    }

    fun getMonthYear(): String {
        val monthFormat = SimpleDateFormat("LLLL")
        val monthName = monthFormat.format(calender.time)
        return "$monthName, $currentYear"
    }
    fun getTheDataListForAnalysis():LiveData<ArrayList<ArrayList<PieEntry>>> {
        return dataListLiveData
    }

    suspend fun loginUser(email:String,password:String):Pair<Boolean,String>{
        return try{
            firebaseAuth.signInWithEmailAndPassword(email, password).await()
            val resultPair = Pair(true,"Login successfull...")
            updateCredentials()
            resultPair
        }catch (e:Exception){
           // Log.d("FIREBASEDATASOURCE",e.message.toString())
            val resultPair = Pair(false,e.message.toString())
            resultPair
        }
    }

    suspend fun signUpUser(email: String,password: String):Pair<Boolean,String>{
        return try{
            firebaseAuth.createUserWithEmailAndPassword(email, password).await()
            val resultPair =Pair(true,"Signup successfull...")
            updateCredentials()
            resultPair

        }catch (e:Exception){
            val resultPair =Pair(false,e.message.toString())
            resultPair
        }

    }

    suspend fun loginWithGoogle(account: GoogleSignInAccount):Pair<Boolean,String>{
        val credentials = GoogleAuthProvider.getCredential(account.idToken ,null)
        return try {
            firebaseAuth.signInWithCredential(credentials).await()
            val result = Pair(true,"Login successfull...")
            updateCredentials()
            result
        }catch (e:Exception){
            val result = Pair(false,e.message.toString())
            result
        }

    }

    suspend fun signUpWithGoogle(account: GoogleSignInAccount):Pair<Boolean,String>{
        val credentials = GoogleAuthProvider.getCredential(account.idToken ,null)
        return try {
            firebaseAuth.signInWithCredential(credentials).await()
            val result = Pair(true,"Signup successfull...")
            updateCredentials()
            result
        }catch (e:Exception){
            val result = Pair(false,e.message.toString())
            result
        }

    }

    private fun updateCredentials(){
        user = firebaseAuth.currentUser
        uid = user?.uid.toString()
        userName = user?.displayName.toString()
        refPath =  "TRACKEXP/USERS/USERRECORDS/${user?.uid.toString()}/RECORDS"
        recordRef = Firebase.firestore.collection(refPath)
        getDatabaseRecords()
    }

    suspend fun addTransactionRecord(transactionRecord: TransactionRecord):Boolean{
        return try {
            recordRef.add(transactionRecord).await()
           // Log.d ("DATASOURCE","ADDED SUCCESSFULLY")
            //log message was just for testing
            true
        }catch (e:Exception){
           // Log.d ("DATASOURCE",e.message.toString())
            false
        }
    }

    suspend fun updateTransactionRecord(oldRecord: TransactionRecord, updateMap: Map<String, Any>):Boolean{

        val recordQuery = recordRef
            .whereEqualTo("amount",oldRecord.amount)
            .whereEqualTo("type",oldRecord.type)
            .whereEqualTo("category",oldRecord.category)
            .whereEqualTo("day",oldRecord.day)
            .whereEqualTo("month",oldRecord.month)
            .whereEqualTo("year",oldRecord.year)
            .whereEqualTo("description",oldRecord.description)
            .get().await()

        if(recordQuery.documents.isNotEmpty()){
            var isSuccess =false
            return try {
                for(doc in recordQuery.documents){

                    recordRef.document(doc.id).set(
                        updateMap,
                        SetOptions.merge()
                    ).await()
                    break
                }
                return true
            }
            catch (e:Exception){
                //Log.d("FIREBASEDATASOURCE",e.message.toString())
                return false
            }
        }else{
           // Log.d("FIREBASEDATASOURCE","Record not found")
            return false
        }



    }

    suspend fun deleteTransactionRecord(oldRecord: TransactionRecord):Boolean{

        val recordQuery = recordRef
            .whereEqualTo("amount",oldRecord.amount)
            .whereEqualTo("type",oldRecord.type)
            .whereEqualTo("category",oldRecord.category)
            .whereEqualTo("day",oldRecord.day)
            .whereEqualTo("month",oldRecord.month)
            .whereEqualTo("year",oldRecord.year)
            .whereEqualTo("description",oldRecord.description)
            .get().await()

        if (recordQuery.documents.isNotEmpty()){

            return try {
                for(doc in recordQuery.documents){

                    recordRef.document(doc.id).delete().await()
                    break
                }
                return true
            }
            catch (e:Exception){
                //Log.d("FIREBASEDATASOURCE",e.message.toString())
                return false
            }
        }
        else{
            return false
        }
    }













}